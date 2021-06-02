import java.sql._
class DBManager {
  val url = "jdbc:mysql://localhost:3306/UPI_Transaction"
  val driver = "com.mysql.cj.jdbc.Driver"
  val username = "root"
  val password = "root"
  Class.forName(driver)

  def createConnection() : Connection = {
    try {
      return DriverManager.getConnection(url, username, password)
    }
    catch {
      case e: Exception => throw e
    }
  }
  def closeDBConnection( connection:Connection, statement:PreparedStatement, resultSet:ResultSet) = {
    try{
      if(connection != null) {
        connection.close()
      }
      if(statement != null) {
        statement.close()
      }
      if(resultSet != null) {
        resultSet.close()
      }
    } catch {
        case e: Exception => throw e
    }
  }
}