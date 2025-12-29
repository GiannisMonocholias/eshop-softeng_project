package gr.softeng.team21.domain;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
public class Date {
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
    public String toString(){
        return getDay() + "/" + getMonth() + "/" + getYear();
    }

}