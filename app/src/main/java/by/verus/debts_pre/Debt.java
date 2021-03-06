package by.verus.debts_pre;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Table(name = "debts")
public class Debt extends Model {

    @Column(name = "name")
    private String name;

    @Column(name = "sum")
    private int sum;

    @Column(name = "timestamp", index = true)
    private Date timestamp;


    public Debt() {
        super();
    }

    public Debt(String name, int sum, Date timestamp) {
        super();

        this.name = name;
        this.sum = sum;
        this.timestamp = timestamp;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }


    public static List<Debt> getAll() {
        return new Select()
                .from(Debt.class)
                .orderBy("timestamp DESC")
                .execute();
    }

    public static Debt findById(long id) {
        return new Select()
                .from(Debt.class)
                .where("Id = ?", id)
                .executeSingle();
    }

    public static void deleteAll() {
        new Delete().from(Debt.class).execute();
    }

    public static void generateDebts(int count) {
        Calendar calendar = Calendar.getInstance();

        ActiveAndroid.beginTransaction();
        try {
            for (int i = 1; i <= count; i++) {
                calendar.add(Calendar.DATE, -2);
                Date date = calendar.getTime();
                new Debt("Vasya " + i, i * 100, date).save();
            }
            ActiveAndroid.setTransactionSuccessful();
        } finally {
            ActiveAndroid.endTransaction();
        }

    }

    @Override
    public String toString() {
        return "debt: [" + this.name + ", " + this.sum + "]";
    }
}
