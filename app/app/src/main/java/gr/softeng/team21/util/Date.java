package gr.softeng.team21.util;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * Represents a date and provides basic date operations.

 * @author PAVLOS GRATSANIS

 */
public class Date implements Comparable<Date> {

    /** The internal LocalDate representation */
    private LocalDate date;

    /**Creates a Date object initialized to the current system date.*/
    public Date() {
        this.date = LocalDate.now();
    }

    /**
     * Creates a Date object with the specified day, month, and year.
     *
     * @param day the day
     * @param month the month
     * @param year the year
     */
    public Date(int day, int month, int year) {
        this.date = LocalDate.of(year, month, day);
    }

    /**
     * Changes the date by adding the specified number of days.
     *
     * @param days the number of days to be added, which can be negative
     */
    public void changeDays(int days) {
        this.date = this.date.plusDays(days);
    }

    /**
     * Returns the day of the month.
     * @return the day
     */
    public int getDay() {
        return date.getDayOfMonth();
    }

    /**
     * Returns the month of the year.
     * @return the month
     */
    public int getMonth() {
        return date.getMonthValue();
    }

    /**
     * Returns the year.
     * @return the year
     */
    public int getYear() {
        return date.getYear();
    }

    /**
     * Compares this Date object with another Date.
     *
     * @param other the Date object to compare with
     * @return a negative, zero, or positive integer as this date
     * is earlier than, equal to, or later than the specified date
     */
    @Override
    public int compareTo(Date other) {
        if (this.getYear() != other.getYear()) {
            return this.getYear() - other.getYear();
        }
        if (this.getMonth() != other.getMonth()) {
            return this.getMonth() - other.getMonth();
        }
        return this.getDay() - other.getDay();
    }

    /**
     * Returns a string representation of the date in the format "day/month/year".
     * @return the formatted date string
     */
    @Override
    public String toString() {
        return getDay() + "/" + getMonth() + "/" + getYear();
    }
}