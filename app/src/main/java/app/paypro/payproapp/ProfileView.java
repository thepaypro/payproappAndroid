package app.paypro.payproapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;

/**
 * Created by kike on 22/11/17.
 */

public class ProfileView extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.profile_view, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        TableRow rowProfile;
        rowProfile = getView().findViewById(R.id.rowNickname);
        rowProfile.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                ProfileView myfragment = new ProfileView();

                fragmentTransaction.replace(R.id.frame_layout, myfragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        } );
    }
}
