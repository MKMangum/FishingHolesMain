package com.mikekmangum.fishingholesmain;

import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    int mRows;
    DatabaseHelper dbHelper;

    public ViewPagerAdapter(FragmentManager fm, int rows) {

        super(fm);
        mRows = rows;
    }




    @Override
    public Fragment getItem(int position) {
        CatchFragment catchFragment = new CatchFragment();
        Bundle bundle = new Bundle();
        Context context = ViewMyCatches.getContext();
        Cursor res = dbHelper.getInstance(context).getCatch(position + 1);

        res.moveToNext();

/*        "\nTime = " + res.getString(1) +
                "\nSpecies = " + res.getString(2) +
                "\nLength = " + res.getString(3) + " inches" +
                "\nWeight = " + res.getString(4) + " pounds" +
                "\nLure Used = " + res.getString(5) +
                "\nLatitude = " + res.getString(6) +
                "\nLongitude = " + res.getString(7) +
                "\nTemperature = " + res.getString(8) +
                "\nConditions = " + res.getString(9));

 */
        bundle.putInt(CatchFragment.ARG_OBJECT, position + 1);
        bundle.putString(CatchFragment.ARG_TIME, res.getString(1));
        bundle.putString(CatchFragment.ARG_SPECIES, res.getString(2));
        bundle.putFloat(CatchFragment.ARG_LENGTH, res.getFloat(3));
        bundle.putFloat(CatchFragment.ARG_WEIGHT, res.getFloat(4));
        bundle.putString(CatchFragment.ARG_LURE, res.getString(5));
        bundle.putFloat(CatchFragment.ARG_LAT, res.getFloat(6));
        bundle.putFloat(CatchFragment.ARG_LON, res.getFloat(7));
        bundle.putFloat(CatchFragment.ARG_TEMP, res.getFloat(8));
        bundle.putString(CatchFragment.ARG_CONDS, res.getString(9));
        bundle.putByteArray(CatchFragment.ARG_BLOB, res.getBlob(10) );
        catchFragment.setArguments(bundle);
        return catchFragment;

    }

    @Override
    public int getCount() {


        return mRows;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        position +=1;

        return "Catch :" + position;
    }


}
