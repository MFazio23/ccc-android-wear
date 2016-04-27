package org.faziodev.cccandroidwear.types;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by MFazio on 4/27/2016.
 */
public class DataApiResult {

    private Date addDate;
    private DataSource source;
    private String notes;

    public enum DataSource { Mobile, Wear, Web }

    public DataApiResult() { }

    public DataApiResult(DataSource source, String notes) {
        this(Calendar.getInstance().getTime(), source, notes);
    }

    public DataApiResult(Date addDate, DataSource source, String notes) {
        this.addDate = addDate;
        this.source = source;
        this.notes = notes;
    }

    public Date getAddDate() {
        return addDate;
    }

    public void setAddDate(Date addDate) {
        this.addDate = addDate;
    }

    public DataSource getSource() {
        return source;
    }

    public void setSource(DataSource source) {
        this.source = source;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "DataApiResult{" +
            "addDate=" + addDate +
            ", source='" + source + '\'' +
            ", notes='" + notes + '\'' +
            '}';
    }
}
