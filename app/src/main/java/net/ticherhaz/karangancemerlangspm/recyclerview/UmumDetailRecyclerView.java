package net.ticherhaz.karangancemerlangspm.recyclerview;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import net.ticherhaz.karangancemerlangspm.ProfileActivity;
import net.ticherhaz.karangancemerlangspm.R;
import net.ticherhaz.karangancemerlangspm.model.RegisteredUser;
import net.ticherhaz.karangancemerlangspm.model.UmumDetail;
import net.ticherhaz.karangancemerlangspm.util.RunTransaction;
import net.ticherhaz.karangancemerlangspm.viewHolder.UmumDetailHolder;
import net.ticherhaz.tarikhmasa.TarikhMasa;

import java.util.List;

import static net.ticherhaz.karangancemerlangspm.util.Others.setStatus;
import static net.ticherhaz.karangancemerlangspm.util.ProgressDialogCustom.dismissProgressDialog;
import static net.ticherhaz.karangancemerlangspm.util.ProgressDialogCustom.showProgressDialog;
import static net.ticherhaz.karangancemerlangspm.util.UserTypeColor.setTextColorUserUmumDetail;

public class UmumDetailRecyclerView extends RecyclerView.Adapter<UmumDetailHolder> {

    /*
     * We already created the UmumDetailHolder which is it is extends from RecyclerView.ViewHolder.
     * as we know most tutorial, they don't split the view holder from this class but we do.
     *
     * 1. Create the variables that we want to use here, so we can communicate from here to UmumDetailActivity.
     */

    //Context
    private Context context;
    //Constraint Layout
    private ConstraintLayout constraintLayout;
    //List
    private List<UmumDetail> umumDetailList;
    //Firebase
    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;
    //Variables
    private String forumUid, umumUid, registeredUidReply, userType;
    private long reputationPower;

    /*
     * This constructor will be called at init view of the activity.
     */
    public UmumDetailRecyclerView(Context context, ConstraintLayout constraintLayout, List<UmumDetail> umumDetailList, DatabaseReference databaseReference, FirebaseUser firebaseUser, String forumUid, String umumUid, String registeredUidReply, String userType, long reputationPower) {
        this.context = context;
        this.constraintLayout = constraintLayout;
        this.umumDetailList = umumDetailList;
        this.databaseReference = databaseReference;
        this.firebaseUser = firebaseUser;
        this.forumUid = forumUid;
        this.umumUid = umumUid;
        this.registeredUidReply = registeredUidReply;
        this.userType = userType;
        this.reputationPower = reputationPower;
    }

    /*
     * When it create view, the view need to be inflater.
     * 1. Create an empty xml layout and design the item.
     * 2. Important, please add attachToRoot to false or it will catch an error.
     * 3. It will return new view which is UmumDetailHolder.
     */
    @NonNull
    @Override
    public UmumDetailHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.umum_detail_item, parent, false);
        return new UmumDetailHolder(view);
    }

    /*
     * When its on bind, we get the position of array umumdetaillist.
     * and then we display the value.
     *
     */
    @Override
    public void onBindViewHolder(@NonNull final UmumDetailHolder holder, final int position) {
        final UmumDetail model = umumDetailList.get(position);

        holder.getTextViewDeskripsi().setText(model.getDeskripsi());
        holder.getTextViewMasaDibalasOleh().setText(TarikhMasa.GetTarikhMasaTimeAgo(model.getPostCreatedDate(), "MY", true, false));
        //Click to the user
        holder.getTextViewUsername().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProfileActivity.class);
                intent.putExtra("registeredUid", model.getRegisteredUid());
                context.startActivities(new Intent[]{intent});
            }
        });

        /*
         * Get the value of the users from registeredUser node.
         * Here we will retrieve user data (this specific) from user database.
         *
         */
        databaseReference.child("registeredUser").child(model.getRegisteredUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    final RegisteredUser registeredUser = dataSnapshot.getValue(RegisteredUser.class);
                    if (registeredUser != null) {

                        final String profileUrlA = registeredUser.getProfileUrl();
                        final String usernameA = registeredUser.getUsername();
                        final String titleTypeA = registeredUser.getTitleType();
                        final String modeA = registeredUser.getMode();
                        final long postCountA = registeredUser.getPostCount();
                        final long reputationA = registeredUser.getReputation();

                        //This part is to display
                        holder.getTextViewUsername().setText(usernameA);
                        holder.getTextViewUserTitle().setText(titleTypeA);
                        holder.getTextViewPos().setText(String.valueOf(postCountA));
                        holder.getTextViewReputation().setText(String.valueOf(reputationA));
                        setStatus(modeA, holder.getTextViewStatus());

                        //Check if profileUrl is null or not
                        if (profileUrlA != null) {
                            Glide.with(context)
                                    .load(profileUrlA)
                                    .into(holder.getImageViewProfile());
                        } else {
                            holder.getImageViewProfile().setImageResource(R.drawable.emblem);
                        }

                        //Call another class to change color
                        setTextColorUserUmumDetail(registeredUser, holder, context);

                        //Edit part
                        //this part, first, we check if the user is already sign in or not and if the user valid, then he can edit his reply
                        //check if the same person, then, he able to edit it
                        if (firebaseUser != null) {
                            if (model.getRegisteredUid().equals(firebaseUser.getUid())) {
                                //then we show the button
                                holder.getTextViewEditReply().setVisibility(View.VISIBLE);

                                //Hide the giving of the reputation
                                holder.getTextViewGiveReputation().setVisibility(View.GONE);

                                holder.getTextViewEditReply().setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        //when we click this one, then we change the layout display from the textview become the edittext.
                                        //so we gone the textview
                                        holder.getTextViewDeskripsi().setVisibility(View.GONE);
                                        //then we display the edit text
                                        holder.getEditTextEdit().setVisibility(View.VISIBLE);
                                        //after that we display the text of the reply.
                                        holder.getEditTextEdit().setText(model.getDeskripsi());
                                        //Then we hide the textview 'reply'
                                        holder.getTextViewEditReply().setVisibility(View.GONE);
                                        //We display with they yes, or cancel to edit
                                        holder.getTextViewEditYes().setVisibility(View.VISIBLE);
                                        holder.getTextViewEditCancel().setVisibility(View.VISIBLE);

                                        //and then we hide the bottom fab
                                        constraintLayout.setVisibility(View.GONE);

                                        //After that we triggered the button yes to edit
                                        holder.getTextViewEditYes().setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                final String newDeskripsi = holder.getEditTextEdit().getText().toString();
                                                if (newDeskripsi.isEmpty() && holder.getEditTextEdit().getText().toString().contains(" ")) {
                                                    Toast.makeText(context, "Sila isi ayat anda...", Toast.LENGTH_LONG).show();
                                                } else {
                                                    //here we triggered to change in the database
                                                    databaseReference.child("umumPos").child(forumUid).child(umumUid).child(model.getUmumDetailUid()).child("deskripsi").setValue(newDeskripsi);
                                                    //After we finish
                                                    //back to normal
                                                    //then we change back all to normal
                                                    //we hide the edittext deskripsi
                                                    holder.getEditTextEdit().setVisibility(View.GONE);
                                                    //we display textview deskripsi
                                                    holder.getTextViewDeskripsi().setVisibility(View.VISIBLE);
                                                    //We hide this button cancel and yes
                                                    holder.getTextViewEditYes().setVisibility(View.GONE);
                                                    holder.getTextViewEditCancel().setVisibility(View.GONE);
                                                    //Then we display back the edit button
                                                    holder.getTextViewEditReply().setVisibility(View.VISIBLE);

                                                    //display fab
                                                    constraintLayout.setVisibility(View.VISIBLE);
                                                }

                                            }
                                        });

                                        //This one is for cancel
                                        holder.getTextViewEditCancel().setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                //then we change back all to normal
                                                //we hide the edittext deskripsi
                                                holder.getEditTextEdit().setVisibility(View.GONE);
                                                //we display textview deskripsi
                                                holder.getTextViewDeskripsi().setVisibility(View.VISIBLE);
                                                //We hide this button cancel and yes
                                                holder.getTextViewEditYes().setVisibility(View.GONE);
                                                holder.getTextViewEditCancel().setVisibility(View.GONE);
                                                //Then we display back the edit button
                                                holder.getTextViewEditReply().setVisibility(View.VISIBLE);

                                                //display fab
                                                constraintLayout.setVisibility(View.VISIBLE);
                                            }
                                        });
                                    }
                                });
                            }
                        }

                        //GIVE REPUTATION
                        holder.getTextViewGiveReputation().setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                AlertDialog alertDialog = new AlertDialog.Builder(context)
                                        .setCancelable(false)
                                        .setTitle("Memberi Reputasi")
                                        .setMessage("Adakah anda ingin memberi reputasi kepada " + registeredUser.getUsername() + "?")
                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(final DialogInterface dialog, int which) {
                                                //Display the progress dialog
                                                showProgressDialog(context);

                                                //First we check the reputationLimit for this guy
                                                databaseReference.child("reputationLimit").child(registeredUidReply).addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        if (dataSnapshot.exists()) {

                                                            final long totalLimitReputation = dataSnapshot.getValue(Long.class);

                                                            //If has
                                                            if (totalLimitReputation > 0) {

                                                                //Then we check if the user already given the reputation baru2 ni or not.
                                                                Query query1 = databaseReference.child("reputationRecord").child(registeredUidReply).limitToLast(2);

                                                                query1.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                        if (dataSnapshot.exists()) {
                                                                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                                                                                String existedUser = dataSnapshot1.getValue(String.class);

                                                                                //then check
                                                                                if (model.getRegisteredUid().equals(existedUser)) {
                                                                                    Toast.makeText(context, "Sila memberi reputasi kepada orang lain sama", Toast.LENGTH_SHORT).show();
                                                                                    dismissProgressDialog();
                                                                                } else if (!model.getRegisteredUid().equals(existedUser)) {

                                                                                    //If yes, then we add the reputation power in this specfic user who post.
                                                                                    databaseReference.child("registeredUser").child(model.getRegisteredUid()).child("reputation").runTransaction(new Transaction.Handler() {
                                                                                        @NonNull
                                                                                        @Override
                                                                                        public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                                                                                            if (mutableData.getValue() == null) {
                                                                                                mutableData.setValue(0);
                                                                                            } else {
                                                                                                mutableData.setValue((Long) mutableData.getValue() + reputationPower); //add the reputation power
                                                                                            }
                                                                                            return Transaction.success(mutableData);
                                                                                        }

                                                                                        @Override
                                                                                        public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
                                                                                            //Hide the progress dialog after finish give the reputation

                                                                                            //After we update the value, then we reduce the reputationLimit for this guy
                                                                                            long afterDeductReputationLimit = totalLimitReputation - 1;
                                                                                            databaseReference.child("reputationLimit").child(registeredUidReply).setValue(afterDeductReputationLimit);

                                                                                            //After that, we catat the data that user already given the reputation.
                                                                                            databaseReference.child("reputationRecord").child(registeredUidReply).push().setValue(model.getRegisteredUid());


                                                                                            dismissProgressDialog();
                                                                                            Toast.makeText(context, "Berjaya memberi reputasi", Toast.LENGTH_SHORT).show();
                                                                                            dialog.cancel();
                                                                                        }
                                                                                    });
                                                                                } else {
                                                                                    Toast.makeText(context, "Masalah", Toast.LENGTH_SHORT).show();
                                                                                    dismissProgressDialog();
                                                                                }

                                                                            }
                                                                        } else {
                                                                            //If no data exist, then we proceed here.
                                                                            //If yes, then we add the reputation power in this specfic user who post.
                                                                            databaseReference.child("registeredUser").child(model.getRegisteredUid()).child("reputation").runTransaction(new Transaction.Handler() {
                                                                                @NonNull
                                                                                @Override
                                                                                public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                                                                                    if (mutableData.getValue() == null) {
                                                                                        mutableData.setValue(0);
                                                                                    } else {
                                                                                        mutableData.setValue((Long) mutableData.getValue() + reputationPower); //add the reputation power
                                                                                    }
                                                                                    return Transaction.success(mutableData);
                                                                                }

                                                                                @Override
                                                                                public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
                                                                                    //Hide the progress dialog after finish give the reputation

                                                                                    //After we update the value, then we reduce the reputationLimit for this guy
                                                                                    long afterDeductReputationLimit = totalLimitReputation - 1;
                                                                                    databaseReference.child("reputationLimit").child(registeredUidReply).setValue(afterDeductReputationLimit);

                                                                                    //After that, we catat the data that user already given the reputation.
                                                                                    databaseReference.child("reputationRecord").child(registeredUidReply).push().setValue(model.getRegisteredUid());


                                                                                    dismissProgressDialog();
                                                                                    Toast.makeText(context, "Berjaya memberi reputasi", Toast.LENGTH_SHORT).show();
                                                                                    dialog.cancel();
                                                                                }
                                                                            });

                                                                        }
                                                                    }

                                                                    @Override
                                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                    }
                                                                });


                                                            }
                                                            //Then if the user already reach 0 total to give reputation
                                                            else {
                                                                dismissProgressDialog();
                                                                Toast.makeText(context, "Maaf, reputatasi hari ini sudah habis, sila tunggu esok", Toast.LENGTH_SHORT).show();
                                                            }

                                                        }


                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                    }
                                                });


                                            }
                                        })
                                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                //if press no
                                                dialog.cancel();
                                            }
                                        })
                                        .create();

                                //If the user is not null when pressing the button give reputation, mean its valid then display the alert dialog
                                if (firebaseUser != null) {
                                    //and check if the user and the one who post is not the same person
                                    if (!model.getRegisteredUid().equals(firebaseUser.getUid())) {
                                        alertDialog.show();
                                    } else {
                                        Toast.makeText(context, "Tidak Boleh Reputasi Diri Sendiri", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(context, "Sila Daftar/Log Masuk Terlebih Dahulu", Toast.LENGTH_SHORT).show();
                                }


                            }
                        });
                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        /*
         * Admin and moderator part.
         * Remove specific umum detail
         */
        if (firebaseUser != null) {
            //Set on Long listener to delete this specific, check the user
            if (userType.equals("admin") || userType.equals("moderator")) {
                holder.getView().setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        AlertDialog alertDialog = new AlertDialog.Builder(context)
                                .setTitle("Options")
                                .setMessage("Are you sure you want to delete this?")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        //Ok after we remove, we need to update the total post of that user who post this reply.
                                        final String umumDetailUid = umumDetailList.get(position).getUmumDetailUid();
                                        FirebaseDatabase.getInstance().getReference().child("umumPos").child(forumUid).child(umumUid).child(umumDetailUid).removeValue();
                                        new RunTransaction().runTransactionRegisteredUserPostCountRemove(databaseReference, registeredUidReply);


                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                })
                                .create();

                        alertDialog.show();
                        return true;
                    }
                });
            }
        }

    }

    /*
     * return the value of the size array umumdetail list.
     */
    @Override
    public int getItemCount() {
        return umumDetailList.size();
    }
}
