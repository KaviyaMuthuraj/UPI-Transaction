import java.io._
import java.sql._
import javax.servlet._
import javax.servlet.http._
import play.api.libs.json._
import scala.collection.mutable.ListBuffer
import scala.collection.immutable.Map
class TransferAmount extends HttpServlet {
    var DBConnection = new DBManager()
    var connection:Connection = null
    var statement:PreparedStatement = null
    var st:PreparedStatement = null
    var statement1:PreparedStatement = null
    var st1:PreparedStatement = null
    var st2:PreparedStatement = null
    var resultSet:ResultSet = null
    @throws(classOf[IOException])  
    @throws(classOf[ServletException])
    override def doPost(request: HttpServletRequest, response: HttpServletResponse)
    {
        connection = DBConnection.createConnection()
        response.setContentType("application/json")
        val out = response.getWriter
        try {
            val reader = request.getReader()
            val line = Json.parse(reader.readLine())
            val json = line("transaction")
            val accountNumber = json("account").as[String]
            if(!ValidateOperations.onlyDigits(accountNumber)) {
                throw new BadRequestException("Incorrect account number..! Please check Your account number..!")
            }
            if(!ValidateOperations.accountExits(accountNumber)) {
                throw new BadRequestException("Account does not Exists..! Please check Your account number..!")
            }
            if(!ValidateOperations.checkUPIState(accountNumber)) {
                throw new BadRequestException("\nYour UPI state is disabled..!")
            }
            val receiverAccount = json("otherAccount").as[String]
            if(!ValidateOperations.onlyDigits(accountNumber)) {
                throw new BadRequestException("Incorrect account number..! Please check Your account number..!")
            }
            if(!ValidateOperations.accountExits(receiverAccount)) {
                throw new BadRequestException("Receiver account does not Exists..! Please check Receiver account number..!")
            }
            if(receiverAccount.equals(accountNumber)) {
                throw new BadRequestException("You are trying to self transfer..!")
            }
            val transferAmount = json("amount").as[Int]
            if (!ValidateOperations.onlyDigits(transferAmount.toString)) {
                throw new BadRequestException("Please Enter valid amount...!")
            }
            if (!ValidateOperations.checkAccountBalance(accountNumber,transferAmount)) {
                throw new BadRequestException("Please check Your account balance...!")
            }

            val date = new Timestamp(java.util.Calendar.getInstance.getTime.getTime)
            statement = connection.prepareStatement("update account_details set account_balance=account_balance+? where account_number=?")
            statement.setObject(1,transferAmount)
            statement.setObject(2,receiverAccount)
            statement.executeUpdate()

            st = connection.prepareStatement("insert into transactions(account_number,amount,date_time,transaction_type,other_account) values(?,?,?,?,?)")
            st.setObject(1,receiverAccount)
            st.setObject(2,transferAmount)
            st.setObject(3,date)
            st.setObject(4,"credited")
            st.setObject(5,accountNumber)
            st.executeUpdate()

            statement1 = connection.prepareStatement("update account_details set account_balance=account_balance-? where account_number=?")
            statement1.setObject(1,transferAmount)
            statement1.setObject(2,accountNumber)
            statement1.executeUpdate()

            st1 = connection.prepareStatement("insert into transactions(account_number,amount,date_time,transaction_type,other_account) values(?,?,?,?,?)")
            st1.setObject(1,accountNumber)
            st1.setObject(2,transferAmount)
            st1.setObject(3,date)
            st1.setObject(4,"debited")
            st1.setObject(5,receiverAccount)
            st1.executeUpdate()

            st2 = connection.prepareStatement("select * from transactions where other_account=? order by transaction_id desc limit 1")
            st2.setObject(1,receiverAccount)
            resultSet = st2.executeQuery()
            while (resultSet.next()) {
                val transactionDetails = Map("transaction" -> Map("id" -> resultSet.getString(6), "otherAccount" -> resultSet.getString(5),
                    "amount" -> resultSet.getString(2), "transactionType" -> resultSet.getString(4),
                    "dateTime" -> resultSet.getString(3)))
                val json: JsValue = Json.toJson(transactionDetails)
                out.println(json)                    
            }                                
        } catch {
            case e: BadRequestException => {
                response.setStatus(400)
                response.getWriter().write(e.getMessage()) 
            }
            case e: Exception => throw e
        } finally {
            DBConnection.closeDBConnection(connection,statement,resultSet)
        }
    }
    @throws(classOf[IOException])  
    @throws(classOf[ServletException])
    override def doGet(request: HttpServletRequest, response: HttpServletResponse)
    {
        connection = DBConnection.createConnection()
        response.setContentType("application/json")
        val out = response.getWriter
        try {
            val accountNumber = request.getParameter("accountNumber")
            if(!ValidateOperations.onlyDigits(accountNumber)) {
                throw new BadRequestException("Incorrect account number..! Please check Your account number..!")
            }
            if(!ValidateOperations.accountExits(accountNumber)) {
                throw new BadRequestException("Account does not Exists..! Please check Your account number..!")
            }
            if(!ValidateOperations.checkUPIState(accountNumber)) {
                throw new BadRequestException("Your UPI state is disabled..!")
            }
            val UPIPIN = request.getParameter("UPIPIN")
            if(!ValidateOperations.onlyDigits(UPIPIN)) {
                throw new BadRequestException("Incorrect UPI PIN...!")
            }
            if(!ValidateOperations.checkUPIPIN(accountNumber,UPIPIN)) {
                throw new BadRequestException("Incorrect UPI PIN...!")
            }
            var transactions = new ListBuffer[Map[String,String]]()                            
            statement = connection.prepareStatement("select ad.user_name,t.date_time,t.amount,t.transaction_type,t.transaction_id from account_details ad,transactions t where ad.account_number=t.other_account and t.account_number=" + accountNumber)
            resultSet = statement.executeQuery()
            while (resultSet.next()) {
                var details = Map("id" -> resultSet.getString(5), "userName" -> resultSet.getString(1),
                    "dateTime" -> resultSet.getString(2),
                    "amount" -> resultSet.getString(3),
                    "transactionType" -> resultSet.getString(4))
                transactions += details                            
            }
            val transactionDetails = Map("transaction" -> transactions)
            val json: JsValue = Json.toJson(transactionDetails)
            out.println(json)                    
        } catch {
            case e: BadRequestException => {
                response.setStatus(400)
                response.getWriter().write(e.getMessage())
            }
            case e: Exception => throw e
        } finally {
            DBConnection.closeDBConnection(connection,statement,resultSet)
        }
    }
}