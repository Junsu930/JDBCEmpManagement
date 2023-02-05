package edu.kh.emp.model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import edu.kh.emp.model.vo.Employee;
import edu.kh.emp.view.EmployeeView;

// DAO(Data Access Object, 데이터 접근 객체)
// -> 데이터베이스에 접근(연결)하는 객체
// --> JDBC 코드 작성

/**
 * @author junsu
 *
 */
/**
 * @author junsu
 *
 */
public class EmployeeDAO {
	
	// JDBC 객체 참조 변수 필드 선언 (class 내부에 공통 사용)
	
	private Connection conn;  // 필드(Heap, 변수가 비어있을 수 없어서 자동으로 JVM에서 기본값으로 초기화함)
	private Statement stmt;
	private ResultSet rs = null; // 따로 null을 명시하지 않아도 JVM에서 기본값으로 초기화한다. 참조형의 초기값은 null
	private PreparedStatement pstmt;
	// Statement의 자식으로 향상된 기능 제공
	// -> ? 기호 (placeholder/위치홀더)를 이용해서
	// SQL에 작성되어지는 리터럴을 동적으로 제어함
	// SQL ? 기호에 추가되는 값은
	// 숫자인 경우 ''없이 대입
	// 문자열이 경우 ''가 자동으로 추가되어 대입 
	
	private String driver = "oracle.jdbc.driver.OracleDriver";
	private String url="jdbc:oracle:thin:@localhost:1521:XE"; 
	private String user = "kh";
	private String pw = "kh1234";
	/*
	 public void method(){
	 	Connection conn2;  // 지역변수(stack 영역에 저장되어 변수가 비어있을 수 있다.)
	 }
	 */
	
	//메소드
	/** 전체 사원 정보 조회 DAO
	 * @return empList 
	 */
	public List<Employee> selectAll() {
		// 1. 결과 저장용 변수 선언
		List<Employee> empList = new ArrayList<>();
		
		try {
			// 2. JDBC 참조 변수에 객체 대입
			Class.forName(driver);
			conn=DriverManager.getConnection(url, user, pw); 
			// 오라클 JDBC 드라이버 객체를 이용하여 DB 접속 방법 생성
			String sql = "SELECT EMP_ID, EMP_NAME, EMP_NO, EMAIL, PHONE, NVL(DEPT_TITLE, '부서없음') DEPT_TITLE, JOB_NAME, SALARY"
					+ " FROM EMPLOYEE NATURAL JOIN JOB LEFT JOIN DEPARTMENT ON (DEPT_ID = DEPT_CODE)";
			
			stmt = conn.createStatement();
			
			rs = stmt.executeQuery(sql);
			
			while(rs.next()) {
			
				int empId = rs.getInt("EMP_ID"); 	
				// EMP_ID 컬럼은 문자열 컬럼이지만 
				// 저장된 값은 모두 숫자
				String empName = rs.getString("EMP_NAME"); 
				String empNo = rs.getString("EMP_NO"); 	
				String email = rs.getString("EMAIL"); 	
				String phone = rs.getString("PHONE"); 
				String departmentTitle = rs.getString("DEPT_TITLE"); 
				String jobName = rs.getString("JOB_NAME"); 
				int salary = rs.getInt("SALARY"); 
				/*
				String deptCode = rs.getString("DEPT_CODE"); 
				String jobCode = rs.getString("JOB_CODE"); 
				String salLevel = rs.getString("SAL_LEVEL"); 
				double bonus = rs.getDouble("BONUS");
				int managerId = rs.getInt("MANAGER_ID");
				*/
				empList.add(new Employee(empId, empName, empNo, email, phone, departmentTitle, jobName, salary));
				
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {
				// 4. 자원 반환
				if(rs!=null) rs.close();
				if(stmt !=null) stmt.close();
				if(conn!=null) conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return empList;
	}
	
	
	public Employee selectEmpId(int empId) {
		
		Employee emp = null;
		// 결과 저장용 변수 선언
		// 만약 조회 결과가 있으면 Employee객체를 생성해서 emp에 대입(null이 아님)
		// 만약 조회 결과가 없으면 emp에 아무것도 대입하지 않음
		
		
		try {
			// 2. JDBC 참조 변수에 객체 대입
			Class.forName(driver);
			conn=DriverManager.getConnection(url, user, pw); 
			String sql = "SELECT EMP_ID, EMP_NAME, EMP_NO, EMAIL, PHONE, NVL(DEPT_TITLE, '부서없음') DEPT_TITLE, JOB_NAME, SALARY"
					+ " FROM EMPLOYEE NATURAL JOIN JOB LEFT JOIN DEPARTMENT ON (DEPT_ID = DEPT_CODE) WHERE EMP_ID = " + empId;
			stmt = conn.createStatement();
			
			rs = stmt.executeQuery(sql);
			
			// 조회 결과가 최대 1행인 경우 불필요한 조건 검사를 줄이기 위해서 if 문 사용 권장
			if(rs.next()) {
				
				String empName = rs.getString("EMP_NAME"); 
				String empNo = rs.getString("EMP_NO"); 	
				String email = rs.getString("EMAIL"); 	
				String phone = rs.getString("PHONE"); 
				String departmentTitle = rs.getString("DEPT_TITLE"); 
				String jobName = rs.getString("JOB_NAME"); 
				int salary = rs.getInt("SALARY"); 
				
				emp = new Employee(empId, empName, empNo, email, phone, departmentTitle, jobName, salary);
			
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {
				// 4. 자원 반환
				if(rs!=null) rs.close();
				if(stmt !=null) stmt.close();
				if(conn!=null) conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return emp;
	}
	
	public Employee selectEmpNo(String empNo) {
		
		Employee emp = null;
		
		try {
			Class.forName(driver);
			conn=DriverManager.getConnection(url, user, pw); 
			String sql = "SELECT EMP_ID, EMP_NAME, EMP_NO, EMAIL, PHONE, NVL(DEPT_TITLE, '부서없음') DEPT_TITLE, JOB_NAME, SALARY"
					+ " FROM EMPLOYEE NATURAL JOIN JOB LEFT JOIN DEPARTMENT ON (DEPT_ID = DEPT_CODE)"
					+ " WHERE EMP_NO = ?";
			// Statement 객체 사용 시 순서
			// SQL작성 -> Statement 생성 -> SQL 수행 후 결과 반환
			
			// PreparedStatement 객체 사용 시 순서
			// SQL작성 -> PreparedStatement 객체 생성 ( ? 가 포함된 SQL을 매개변수로 사용) 
			// -> ?에 알맞은 값 대입
			// -> SQL 수행 후 결과 반환
			
			// PreparedStatement 객체 생성
			
			pstmt = conn.prepareStatement(sql);
			
			// ?에 알맞은 값 대입
			pstmt.setString(1, empNo);
			
			// SQL 수행 후 결과 반환
			rs = pstmt.executeQuery();
			
			// PreparedStatement는 객체 생성 시 이미 SQL이 담겨져 있는 상태이므로
			// SQL 수행 시 매개변수로 전달할 필요가 없다
			// pstmt.executeQuery(sql);
			// -> ? 에 작성되어있던 값이 모두 사라져 수행 시 오류 발생
			
			
			if(rs.next()) {
				int empId = rs.getInt("EMP_ID"); 	
				String empName = rs.getString("EMP_NAME"); 
				// String empNo = rs.getString("EMP_NO");  파라미터와 같은 값이므로 불필요하다
				String email = rs.getString("EMAIL"); 	
				String phone = rs.getString("PHONE"); 
				String departmentTitle = rs.getString("DEPT_TITLE"); 
				String jobName = rs.getString("JOB_NAME"); 
				int salary = rs.getInt("SALARY"); 
				
				emp = new Employee(empId, empName, empNo, email, phone, departmentTitle, jobName, salary);
			}
			
		}catch(Exception e) {
			
		}finally {
			if(rs!=null)
				try {
					// JDBC 객체 참조 변수 닫아준다.
					if(rs!=null) rs.close();
					if(pstmt !=null) pstmt.close();
					if(conn!=null) conn.close();
					
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
		return emp;
	}
	
	/**
	 * @param emp
	 * @return result(INSERT 성공한 행의 개수 반환)
	 */
	public int insertEmployee(Employee emp){
		int result = 0;
		
		try {
			Class.forName(driver);
			conn=DriverManager.getConnection(url, user, pw); 
			
			// ** DML 수행할 예정 **
			// 트랜잭션에 DML 구문이 임시 저장
			// --> 정상적인 DML인지를 판별해서 개발자가 직접 commit, rollback을 수행
			
			// Connection 객체 생서 시
			// AutoCommit이 활성화되어 있는 상태이기 때문에
			// 이를 해제하는 코드를 추가
			conn.setAutoCommit(false); // AutoCommit 비활성화
			// AutoCommit 비활성화 해도
			// conn.close(); 구문이 수행되면 자동으로 Commit이 수행됨
			// -> close() 수행 전에 제어 코드를 작성해야 한다.
			
			
			String sql = "INSERT INTO EMPLOYEE VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, SYSDATE, NULL, DEFAULT)";
			
			pstmt = conn.prepareStatement(sql);
	
			pstmt.setInt(1, emp.getEmpId());
			pstmt.setString(2, emp.getEmpName());
			pstmt.setString(3, emp.getEmpNo());
			pstmt.setString(4, emp.getEmail());
			pstmt.setString(5, emp.getPhone());
			pstmt.setString(6, emp.getDeptCode());
			pstmt.setString(7, emp.getJobCode());
			pstmt.setString(8, emp.getSalLevel());
			pstmt.setInt(9, emp.getSalary());
			pstmt.setDouble(10, emp.getBonus());
			pstmt.setInt(11, emp.getManagerId());
			
			// SQL 수행 후 결과 반환 받기
			result = pstmt.executeUpdate();
			// executeQuery() : SELECT 수행 후 ResultSet 반환
			// executeUpdate() : DML(INSER, UPDATE, DELETE) 수행 후 결과 행 개수 반환
			
			// ** 트랜잭션 제어 처리**
			// -> DML 성공 여부에 따라서 commit, rollback 제어
			if(result > 0) conn.commit();
			else conn.rollback();
			
		}catch(Exception e) {
		}finally {
			if(rs!=null)
				try {
					if(pstmt !=null) pstmt.close();
					if(conn!=null) conn.close();
					
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
		
		return result;
	}
	
	
	/** 입력 받은 급여 이상을 받는 모든 사원 정보 조회 DAO
	 * @param sal
	 * @return tempList
	 */
	public List<Employee> selectSalaryEmp(int sal){
		List<Employee> tempList = new ArrayList<>();
		// 결과 저장용 변수 만들기	
		
		try {
			Class.forName(driver);
			conn=DriverManager.getConnection(url, user, pw); 
			String sql = "SELECT EMP_ID, EMP_NAME, EMP_NO, EMAIL, PHONE, NVL(DEPT_TITLE, '부서없음') DEPT_TITLE, JOB_NAME, SALARY"
					+" FROM EMPLOYEE NATURAL JOIN JOB LEFT JOIN DEPARTMENT ON (DEPT_ID = DEPT_CODE) WHERE SALARY >= ?";
			pstmt = conn.prepareStatement(sql);
		
			pstmt.setInt(1, sal);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				int empId = rs.getInt("EMP_ID"); 	
				String empName = rs.getString("EMP_NAME"); 
				String empNo = rs.getString("EMP_NO"); 
				String email = rs.getString("EMAIL"); 	
				String phone = rs.getString("PHONE"); 
				String departmentTitle = rs.getString("DEPT_TITLE"); 
				String jobName = rs.getString("JOB_NAME"); 
				int salary = rs.getInt("SALARY"); 
				
				tempList.add(new Employee(empId, empName, empNo, email, phone, departmentTitle, jobName, salary));
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(rs!=null) rs.close();
				if(stmt!=null) stmt.close();
				if(conn!=null) conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return tempList;
		
	}
		
	
	public Map<String, Integer> selectDeptTotalSalary(){
		
		// 결과 저장용 변수 
		Map<String, Integer> sumlist = new LinkedHashMap<>();
		try {
			
			Class.forName(driver);
			conn=DriverManager.getConnection(url, user, pw); 
			stmt = conn.createStatement();
			
			String sql = "SELECT NVL(DEPT_CODE, '부서없음') DEPT_CODE, SUM(SALARY) SUM_SAL FROM EMPLOYEE GROUP BY DEPT_CODE ORDER BY DEPT_CODE";
			rs = stmt.executeQuery(sql);
			
			while(rs.next()) {
				String deptCode = rs.getString("DEPT_CODE");
				int sumSal = rs.getInt("SUM_SAL");
				
				sumlist.put(deptCode, sumSal);
			}
			
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(rs!=null) rs.close();
				if(stmt!=null) stmt.close();
				if(conn!=null) conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return sumlist;
		
	}
	
	public Map<String, Double> selectJobAvgSalary() {
		
		Map<String, Double> avglist = new LinkedHashMap<>();
		
		try {
			Class.forName(driver);
			conn=DriverManager.getConnection(url, user, pw); // 커넥션 생성해서 얻어오기
			stmt = conn.createStatement();
			
			String sql = "SELECT JOB_NAME, ROUND(AVG(SALARY), 1) AVG_SAL FROM EMPLOYEE NATURAL JOIN JOB GROUP BY JOB_NAME, JOB_CODE ORDER BY JOB_CODE";
			rs = stmt.executeQuery(sql);
			
			while(rs.next()) {
				String jobCode = rs.getString("JOB_NAME");
				double avgSal = rs.getDouble("AVG_SAL");
				
				avglist.put(jobCode, avgSal);
				
			}
			
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(rs!=null) rs.close();
				if(stmt!=null) stmt.close();
				if(conn!=null) conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
		return avglist;
	}
	
	public List<Employee> selectDeptEmp(String dept) {
		
		List<Employee> tempList = new ArrayList<> ();
		try {
			Class.forName(driver);
			conn=DriverManager.getConnection(url, user, pw); 
			String sql = "SELECT EMP_ID, EMP_NAME, EMP_NO, EMAIL, PHONE, NVL(DEPT_TITLE, '부서없음') DEPT_TITLE, JOB_NAME, SALARY"
					+" FROM EMPLOYEE NATURAL JOIN JOB LEFT JOIN DEPARTMENT ON (DEPT_ID = DEPT_CODE) WHERE DEPT_TITLE = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dept);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				int empId = rs.getInt("EMP_ID"); 	
				String empName = rs.getString("EMP_NAME"); 
				String empNo = rs.getString("EMP_NO"); 
				String email = rs.getString("EMAIL"); 	
				String phone = rs.getString("PHONE"); 
				String jobName = rs.getString("JOB_NAME"); 
				int salary = rs.getInt("SALARY"); 
				
				tempList.add(new Employee(empId, empName, empNo, email, phone, dept, jobName, salary)); //List에 담기
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(rs!=null) rs.close();
				if(stmt!=null) stmt.close();
				if(conn!=null) conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return tempList;
	}
	
	/** 사번이 일치하는 사원 정보 수정 DAO
	 * @param emp
	 * @return reslt
	 */
	public int updateEmployee(Employee emp) {
		int result = 0;
		try {
			Class.forName(driver);
			conn=DriverManager.getConnection(url, user, pw); 
			conn.setAutoCommit(false); // AutoCommit 비활성화
			
			String sql = "UPDATE EMPLOYEE SET EMAIL = ?, PHONE = ?, SALARY = ? WHERE EMP_ID = ?";
			// PreparedStatment 생성
			pstmt = conn.prepareStatement(sql);
			// ?에 알맞는 값 세팅
			pstmt.setString(1, emp.getEmail());
			pstmt.setString(2, emp.getPhone());
			pstmt.setInt(3, emp.getSalary());
			pstmt.setInt(4, emp.getEmpId());
			
			result = pstmt.executeUpdate();
			
			if(result == 0 ) conn.rollback();
			else conn.commit();
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally{
				try {
					if(pstmt!=null) pstmt.close();
					if(conn!=null) conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		return result;
	}
	
	public int deleteEmployee(int empId) {
		int result = 0;
		try {
		
			Class.forName(driver);
			conn = DriverManager.getConnection(url, user, pw);
			conn.setAutoCommit(false);
			
			String sql = "DELETE FROM EMPLOYEE WHERE EMP_ID = ?";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, empId);
			
			result = pstmt.executeUpdate();
			
			if (result== 0) conn.rollback();
			else conn.commit();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
				try {
					if(pstmt!=null) pstmt.close();
					if(conn!=null) conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		return result;
	}
}









