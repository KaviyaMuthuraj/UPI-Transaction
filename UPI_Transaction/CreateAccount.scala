import java.io._
import java.sql._
import javax.servlet._
import javax.servlet.http._
import play.api.libs.json._
class CreateAccount extends HttpServlet {
    val DBConnection = new DBManager()
    var connection:Connection = null
    var statement:PreparedStatement = null
    var statement1:PreparedStatement = null
    var resultSet:ResultSet = null
    @throws(classOf[IOException])  
    @throws(classOf[ServletException])
    @throws(classOf[BadRequestException])
    override def doPost(request: HttpServletRequest, response: HttpServletResponse)
    {
        connection = DBConnection.createConnection()
        response.setContentType("text/html")
        val out = response.getWriter
        try {
            val reader = request.getReader()
            val line = Json.parse(reader.readLine())
            val json = line("accountDetail")
            val userName = json("userName").as[String]
            if(!ValidateOperations.checkUserName(userName)) {
                throw new BadRequestException("User name atleast have 3 characters and does not contain numbers and special Character..!")
            }
            val accountNumber = json("id").as[String]
            if(!ValidateOperations.checkAccountNumber(accountNumber)) {
                throw new BadRequestException("Account number must have 10 numbers and does not contain alphabets and special Character...!")
            }
            if(ValidateOperations.accountExits(accountNumber)) {
                throw new BadRequestException("Account number already Exists..! Please check Your account number..!")
            }
            val UPIPINStr = json("UPIPIN").as[String]
            if(!ValidateOperations.validateUPIPIN(UPIPINStr)) {
                throw new BadRequestException("UPI PIN must have 6 numbers and does not contain alphabets and special Character..!")
            }
            val UPIPIN = UPIPINStr.toInt                       
            statement = connection.prepareStatement("insert into account_details(account_number,user_name,UPI_pin) values(?,?,?)")
            statement.setString(1,accountNumber)
            statement.setString(2,userName)
            statement.setInt(3,UPIPIN)
            statement.executeUpdate()   
            var accountDetails = Map("account-detail" -> Map("id" -> accountNumber, "userName" -> userName,
                "UPIPIN" -> UPIPINStr, "UPIState" -> "true",
                "accountBalance" -> "0"))
            val jsonVal: JsValue = Json.toJson(accountDetails)
            out.println(jsonVal)     
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
    @throws(classOf[BadRequestException])
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
            val UPIPIN = request.getParameter("UPIPIN")
            if(!ValidateOperations.onlyDigits(UPIPIN)) {
                throw new BadRequestException("Incorrect UPI PIN...!")
            }
            if(!ValidateOperations.checkUPIPIN(accountNumber,UPIPIN)) {
                throw new BadRequestException("Incorrect UPI PIN...!")
            }
            statement = connection.prepareStatement("select * from account_details where account_number="+accountNumber+" and UPI_pin="+UPIPIN)
            resultSet = statement.executeQuery()
            while (resultSet.next()) {
                var accountDetails = Map("account-detail" -> Map("id" -> resultSet.getString(1), "userName" -> resultSet.getString(2),
                    "UPIPIN" -> resultSet.getString(3), "UPIState" -> resultSet.getString(4),
                    "accountBalance" -> resultSet.getString(5)))
                val json: JsValue = Json.toJson(accountDetails)
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
    @throws(classOf[BadRequestException])
    override def doPut(request: HttpServletRequest, response: HttpServletResponse) 
    {
        connection = DBConnection.createConnection()
        response.setContentType("application/json")
        val out = response.getWriter
        try {
            val reader = request.getReader()
            val line = Json.parse(reader.readLine())
            val json = line("accountDetail")
            val UPIState = json("UPIState").as[Boolean]
            val array = request.getRequestURL().toString.split("accountDetails/")
            val accountNumber = array(1)
            if(!ValidateOperations.onlyDigits(accountNumber)) {
                throw new BadRequestException("Incorrect account number..! Please check Your account number..!")
            }
            if(!ValidateOperations.accountExits(accountNumber)) {
                throw new BadRequestException("Account does not Exists..! Please check Your account number..!")
            }
            statement = connection.prepareStatement("update account_details set UPI_state='"+UPIState+"' where account_number="+accountNumber)
            statement.executeUpdate()
            statement1 = connection.prepareStatement("select * from account_details where account_number=?")
            statement1.setObject(1,accountNumber)
            resultSet = statement1.executeQuery()
            while(resultSet.next()) {
                var accountDetails = Map("account-detail" -> Map("id" -> resultSet.getString(1), "userName" -> resultSet.getString(2),
                    "UPIPIN" -> resultSet.getString(3), "UPIState" -> resultSet.getString(4),
                    "accountBalance" -> resultSet.getString(5)))
                val json: JsValue = Json.toJson(accountDetails)
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
}