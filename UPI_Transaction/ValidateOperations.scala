import java.sql._
object ValidateOperations {
    var DBConnection = new DBManager()
    var connection:Connection = null
    var statement:PreparedStatement = null
    var resultSet:ResultSet = null
    //function to check the UPI PIN
    def checkUPIPIN(accountNumber:String, UPIPin:String): Boolean = {
        connection = DBConnection.createConnection()
        try {
            statement = connection.prepareStatement("select * from account_details where account_number="+accountNumber+" and UPI_pin="+UPIPin)
            resultSet = statement.executeQuery()
            while (resultSet.next()) {
                return true
            }
        } catch {
            case e: Exception => throw e
        } finally {
            DBConnection.closeDBConnection(connection,statement,resultSet)
        }
        false
    }
    //function for check the account balance
    def checkAccountBalance(accountNumber:String, transferAmount:Long): Boolean = {
        connection = DBConnection.createConnection()
        try {
            statement = connection.prepareStatement("select account_balance from account_details where account_number="+accountNumber)
            resultSet = statement.executeQuery()
            while (resultSet.next()) {
                var accountBalance = resultSet.getLong(1)
                if(accountBalance >= transferAmount) {
                    return true
                } else {
                    return false
                }
            }
        } catch {
            case e: Exception => throw e
        } finally {
            DBConnection.closeDBConnection(connection,statement,resultSet)
        }
        false
    }
    //function to check UPI State
    def checkUPIState(accountNumber:String): Boolean = {
        connection = DBConnection.createConnection()
        try {
            statement = connection.prepareStatement("select UPI_state from account_details where account_number="+accountNumber)
            resultSet = statement.executeQuery()
            while (resultSet.next()) {
                return resultSet.getBoolean(1)
            }
        } catch {
            case e: Exception => throw e
        } finally {
            DBConnection.closeDBConnection(connection,statement,resultSet)
        }
        false
    }
    //function to check the username
    def checkUserName(userName: String): Boolean = {
        if(userName.length > 3 && onlyAlphabets(userName)) {
            return true
        }
        false
    }
    //function to check the account number
    def checkAccountNumber(accountNumber: String): Boolean = {
        if(accountNumber.length == 10 && onlyDigits(accountNumber)){
            return true
        } 
        false
    }
    //function to check the UPI PIN
    def validateUPIPIN(UPIPIN: String): Boolean = {
        if(UPIPIN.length == 6 && onlyDigits(UPIPIN)){
            return true
        } 
        false
    }
    //function to check account Exists or not
    def accountExits(accontNumber:String): Boolean = {
        connection = DBConnection.createConnection()
        try {
            statement = connection.prepareStatement("select * from account_details where account_number="+accontNumber)
            resultSet = statement.executeQuery()
            while (resultSet.next()){
                return true
            }
        } catch {
            case e: Exception => throw e
        } finally {
            DBConnection.closeDBConnection(connection,statement,resultSet)
        }
        false
    }
    //function to check a string has only digit
    def onlyDigits(s: String): Boolean = s.forall(_.isDigit)
    //function to check a string has only alphabets
    def onlyAlphabets(s: String): Boolean = s.forall(_.isLetter)
}