package by.verus.debts;


import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.activeandroid.query.Update;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;

import java.text.DateFormat;
import java.util.Locale;

import static com.basgeekball.awesomevalidation.ValidationStyle.BASIC;

public class DebtHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private TextView nameTv;
    private TextView sumTv;
    private TextView dateTv;
    private Debt mDebt;

    public DebtHolder(View itemView) {
        super(itemView);

        nameTv = (TextView) itemView.findViewById(R.id.nameTv);
        sumTv = (TextView) itemView.findViewById(R.id.sumTv);
        dateTv = (TextView) itemView.findViewById(R.id.dateTv);

        itemView.setOnClickListener(this);
    }

    public void bindDebt(Debt debt) {
        mDebt = debt;

        nameTv.setText(debt.getName());
        sumTv.setText(String.valueOf(debt.getSum()));

        DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.ENGLISH);
        String strDate = df.format(debt.getTimestamp());
        dateTv.setText(strDate);
    }

    @Override
    public void onClick(View v) {
        final Context context = v.getContext();
        View formElementsView = View.inflate(context, R.layout.dialog_add_update_debt, null);

        final EditText nameEt = (EditText) formElementsView.findViewById(R.id.nameEt);
        final EditText sumEt = (EditText) formElementsView.findViewById(R.id.sumEt);

        nameEt.append(mDebt.getName());
        sumEt.append(String.valueOf(mDebt.getSum()));

        final AwesomeValidation awesomeValidation = new AwesomeValidation(BASIC);
        awesomeValidation.addValidation(nameEt, RegexTemplate.NOT_EMPTY, context.getString(R.string.err_required));
        awesomeValidation.addValidation(sumEt, RegexTemplate.NOT_EMPTY, context.getString(R.string.err_required));

        final AlertDialog dialog = new AlertDialog.Builder(context)
                .setView(formElementsView)
                .setTitle("Edit debt")
                .setPositiveButton("Save", null)
                .setNegativeButton("Cancel", null)
                .create();

        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (awesomeValidation.validate()) {
                    new Update(Debt.class)
                            .set("name=?," + "sum=?", nameEt.getText(), Integer.valueOf(sumEt.getText().toString()))
                            .where("Id=?", mDebt.getId())
                            .execute();

                    dialog.dismiss();
                }
            }
        });
    }
}
