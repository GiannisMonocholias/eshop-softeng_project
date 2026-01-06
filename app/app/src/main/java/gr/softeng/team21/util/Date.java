package gr.softeng.team21.util;

import java.time.LocalDate;

public class Date implements Comparable<Date>{
    private LocalDate date;

    public Date() {
        this.date = LocalDate.now();
    }

    public Date(int day, int month, int year) {
        this.date = LocalDate.of(year, month, day);
    }

    public void changeDays(int days) {
        this.date = this.date.plusDays(days);
    }

    public int getDay() {
        return date.getDayOfMonth();
    }

    public int getMonth() {
        return date.getMonthValue();
    }

    public int getYear() {
        return date.getYear();
    }

    @Override
    public int compareTo(Date other){
        if(this.getYear() != other.getYear()){
            return this.getYear() - other.getYear();
        }
        if(this.getMonth() != other.getMonth()){
            return this.getMonth() - other.getMonth();
        }
        return this.getDay() - other.getDay();
    }

    @Override
    public String toString(){
        return getDay() + "/" + getMonth() + "/" + getYear();
    }

}