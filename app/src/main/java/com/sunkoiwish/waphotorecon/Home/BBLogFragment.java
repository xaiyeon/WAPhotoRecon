package com.sunkoiwish.waphotorecon.Home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.sunkoiwish.waphotorecon.FireBase.FireBaseFragment;
import com.sunkoiwish.waphotorecon.Models.SysMessage;
import com.sunkoiwish.waphotorecon.Models.UserPhoto;
import com.sunkoiwish.waphotorecon.R;

/**
 * Created by Xaiyeon on 10/24/2017.
 */

public class BBLogFragment extends Fragment{

    private static final String TAG = "BBLogFragment";
    // Define FireBase
    private FirebaseApp app;
    private FirebaseAuth auth;

    // Define the photos Firebase DatabaseReference
    private DatabaseReference database_syslog;

    RecyclerView rec_view;
    private LinearLayoutManager mLayoutManager;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bblog, container, false);

        // Firebase instances
        app = FirebaseApp.getInstance();
        auth = FirebaseAuth.getInstance(app);

        // get from out database of the usersystemlog messages
        database_syslog = FirebaseDatabase.getInstance().getReference("usermessagelogs").child(auth.getCurrentUser().getUid().toString());

        // Now set the properties of the LinearLayoutManager
        mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);

        rec_view = (RecyclerView) view.findViewById(R.id.FireBaseDBSysLog_RecView);
        rec_view.setLayoutManager(mLayoutManager);

        // Don't need to check if we are signed in because you have to sign in...

        // start firebase recylce... You must define the parameters you specified below to let it know what you doing.
        FirebaseRecyclerAdapter<SysMessage, BBLogFragment.UserSysMessageViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<SysMessage, BBLogFragment.UserSysMessageViewHolder>(
                SysMessage.class,
                R.layout.layout_out_system_messages,
                BBLogFragment.UserSysMessageViewHolder.class,
                database_syslog
        ) {
            @Override
            protected void populateViewHolder(BBLogFragment.UserSysMessageViewHolder viewHolder, SysMessage model, int position) {

                viewHolder.txt_device_name.setText(model.getDevice_name());
                viewHolder.txt_date.setText(model.getCreate_date());
                viewHolder.txt_message.setText(model.getMessage()); // the message
//                Picasso.with(getContext()).load(model.getImgdata_url()).into(viewHolder.imgUsersimg);

            }
        };

        // now we set it, and it populates!
        rec_view.setAdapter(firebaseRecyclerAdapter);

        return view;
    }


    // UserSysMessageViewHolder is used as a layout to populate for the recycler view.

    public static class UserSysMessageViewHolder extends RecyclerView.ViewHolder{

        TextView txt_device_name;
        TextView txt_date;
        TextView txt_message;

        public UserSysMessageViewHolder(View itemView){
            super(itemView);
            txt_device_name = (TextView) itemView.findViewById(R.id.lay_sysmes_txtviewdevicename);
            txt_date = (TextView) itemView.findViewById(R.id.lay_sysmes_createdate);
            txt_message = (TextView) itemView.findViewById(R.id.lay_sysmes_txtviewmessage);
        }


    } // End of UserSysMessageViewHolder


}
