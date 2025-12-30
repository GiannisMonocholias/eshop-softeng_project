package gr.softeng.team21.domain;

import gr.softeng.team21.memorydao.EmployeeDAOMemory;

public class Employee extends User{
    private String employeeId;
    private Date hireDate;
    private EmployeeState EmployeeState;
    private int WorkingHours;
    private int salary;
    private int bonus;



    public Employee(String username, String firstname, String password, String lastname, String phoneNumber, EmailAddress emailaddress, String employeeId, int bonus, int salary, int workingHours, EmployeeState employeeState, Date hireDate) {
        super(username, firstname, password, lastname,phoneNumber, emailaddress);
        setEmployeeId(employeeId);
        setBonus(bonus);
        setSalary(salary);
        setWorkingHours(workingHours);
        setEmployeeState(employeeState);
        setHireDate(hireDate);
        EmployeeDAOMemory.getInstance().addEmployee(this);
    }



    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {this.employeeId = employeeId;}

    public int getBonus() {
        return bonus;
    }

    public void setBonus(int bonus) {
        this.bonus = bonus;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public EmployeeState getEmployeeState() {
        return EmployeeState;
    }

    public void setEmployeeState(EmployeeState employeeState) {EmployeeState = employeeState;}

    public int getWorkingHours() {
        return WorkingHours;
    }

    public void setWorkingHours(int workingHours) {
        WorkingHours = workingHours;
    }

    public Date getHireDate() {
        return hireDate;
    }

    public void setHireDate(Date hireDate) {
        if(this.hireDate == null)
            this.hireDate = hireDate;
    }
}


