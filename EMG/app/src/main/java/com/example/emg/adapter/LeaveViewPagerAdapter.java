package com.example.emg.adapter;

import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.emg.admin.leave.HistoryFragment;
import com.example.emg.admin.leave.LeaveFragment;

public class LeaveViewPagerAdapter extends FragmentStateAdapter {
    public LeaveViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch(position){
            case 0:
                return new LeaveFragment();
            case 1:
                return new HistoryFragment();
            default:
                return new LeaveFragment();

        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
