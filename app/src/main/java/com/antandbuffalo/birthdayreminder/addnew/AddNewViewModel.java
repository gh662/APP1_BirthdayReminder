package com.antandbuffalo.birthdayreminder.addnew;

import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.antandbuffalo.birthdayreminder.Constants;
import com.antandbuffalo.birthdayreminder.DateOfBirth;
import com.antandbuffalo.birthdayreminder.Util;
import com.antandbuffalo.birthdayreminder.database.DateOfBirthDBHelper;
import com.antandbuffalo.birthdayreminder.model.BirthdayInfo;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class AddNewViewModel extends ViewModel {

    Calendar cal = Util.getCalendar();

    // months starts from 0 for Jan
    DateOfBirth dateOfBirth;
    BirthdayInfo birthdayInfo;

    public void initDefaults() {
//        cal.setTime(new Date());
//        date = cal.get(Calendar.DATE);
//        month = cal.get(Calendar.MONTH);
//        year = cal.get(Calendar.YEAR);
        //year = Constants.REMOVE_YEAR_VALUE;

        dateOfBirth = new DateOfBirth();
        birthdayInfo = new BirthdayInfo();
        birthdayInfo.name = "";
        birthdayInfo.date = "";
        birthdayInfo.month = "0";
        birthdayInfo.year = Constants.REMOVE_YEAR_VALUE.toString();
        birthdayInfo.isRemoveYear = true;
    }

    public List getMonths() {
        return Util.getMonths();
    }

    public Boolean isDOBAvailable(DateOfBirth dob) {
        return !DateOfBirthDBHelper.isUniqueDateOfBirthIgnoreCase(dob);
    }

    public Boolean isNameEmpty() {
        if(dateOfBirth.getName() == null) {
            return true;
        }
        return dateOfBirth.getName().trim().equalsIgnoreCase("");
    }

    public String getFileName() {
        return Util.fileToLoad(dateOfBirth.getName());
    }

    public String loadFromFileWithName(String fileName) {
        return Util.readFromAssetFile(fileName);
    }

    public Boolean setDateOfBirth(BirthdayInfo birthdayInfo) {
        int intDate, intMonth, intYear;

        birthdayInfo.year = birthdayInfo.isRemoveYear? Constants.REMOVE_YEAR_VALUE.toString() : birthdayInfo.year;
        dateOfBirth.setName(birthdayInfo.name);

        try {
            intDate = Integer.parseInt(birthdayInfo.date);
            intMonth = Integer.parseInt(birthdayInfo.month);
            intYear = Integer.parseInt(birthdayInfo.year);
            cal.set(intYear, intMonth, intDate);
            Date plainDate = cal.getTime();

            dateOfBirth.setDobDate(plainDate);
            dateOfBirth.setRemoveYear(birthdayInfo.isRemoveYear);
            dateOfBirth.setAge(Util.getAge(dateOfBirth.getDobDate()));
            return true;

        }
        catch (Exception e) {
            Log.e("PARSE_INT", e.getLocalizedMessage());
            return false;
        }
    }

    public void saveToDB() {
        DateOfBirthDBHelper.insertDOB(dateOfBirth);
        //Util.updateFile(dateOfBirth);
    }

    public boolean isValidDateOfBirth(BirthdayInfo birthdayInfo) {
        int intDate, intMonth, intYear;
        Calendar calendar = Calendar.getInstance();
        try {
            intDate = Integer.parseInt(birthdayInfo.date);
            intMonth = Integer.parseInt(birthdayInfo.month);
            intYear = Integer.parseInt(birthdayInfo.year);
            calendar.set(intYear, intMonth, intDate);
            Date plainDate = calendar.getTime();
            calendar.setTime(plainDate);

            if(calendar.get(Calendar.DATE) != intDate || calendar.get(Calendar.MONTH) != intMonth || calendar.get(Calendar.YEAR) != intYear) {
                return false;
            }
            return  true;
        }
        catch (Exception e) {
            Log.e("PARSE_INT", e.getLocalizedMessage());
            return false;
        }
    }

    public void setBirthdayInfo(String name, String date, Integer month, String year, Boolean flag) {
        birthdayInfo.name = (name != null)? name : birthdayInfo.name;
        birthdayInfo.date = (date != null)? date : birthdayInfo.date;
        birthdayInfo.month = (month != null)? month.toString() : birthdayInfo.month;
        birthdayInfo.year = (year != null)? year : birthdayInfo.year;
        birthdayInfo.isRemoveYear = (flag != null)? flag : birthdayInfo.isRemoveYear;
    }
    public void setBirthdayInfoName(String name) {
        setBirthdayInfo(name, null, null, null, null);
    }
    public void setBirthdayInfoDate(String date) {
        setBirthdayInfo(null, date, null, null, null);
    }
    public void setBirthdayInfoMonth(Integer month) {
        setBirthdayInfo(null, null, month, null, null);
    }
    public void setBirthdayInfoYear(String year) {
        setBirthdayInfo(null, null, null, year, null);
    }
    public void setBirthdayInfoRemoveYear(Boolean flag) {
        setBirthdayInfo(null, null, null, null, flag);
    }



    public void clearInputs() {
        initDefaults();
    }
}
