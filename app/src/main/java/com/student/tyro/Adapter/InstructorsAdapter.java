package com.student.tyro.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import com.student.tyro.InstructorsDetailsActivity;
import com.student.tyro.Util.Constants_Urls;
import com.student.tyro.R;
import com.student.tyro.Model.Instructor;

import java.util.ArrayList;

public class InstructorsAdapter extends RecyclerView.Adapter<InstructorsAdapter.ViewHolder>{
    Context context;
    private ArrayList<Instructor> instructorModalArrayList;
    private ArrayList<Instructor> items;

    public InstructorsAdapter(Context applicationContext, ArrayList<Instructor> instructorslist) {
        this.context = applicationContext;
        this.instructorModalArrayList = instructorslist;
    }


    @Override
    public InstructorsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InstructorsAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.row_instructors, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull InstructorsAdapter.ViewHolder holder, int position) {
        // setting data to our text views from our modal class.
        Instructor ins = instructorModalArrayList.get(position);
        Log.i("array",""+ins);
        holder.name.setText(ins.getInstruct_firstname());
        holder.lang.setText(ins.getInstruct_language());

        float f=Float.parseFloat(ins.getInstruct_rate());
        System.out.println(f);
        String mytext=Float.toString(f);
        holder.rating.setText(mytext);
        holder.distance.setText(ins.getInstruct_distance()+"Km");
        // holder.date.setText(courses.getUsername());
        Picasso.get().load(Constants_Urls.pic_base_url+ins.getInstruct_img())
                .placeholder(R.drawable.user)
                .into(holder.img_icon);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  fragment = new UsersFragment();
                FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frameLayout, fragment);
                fragmentTransaction.commit();*/
            //Toast.makeText(context,""+ins.getInstruct_id(),Toast.LENGTH_LONG).show();

                Intent i=new Intent(context, InstructorsDetailsActivity.class);
                i.putExtra("instruct_id", ins.getInstruct_id());

                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return instructorModalArrayList.size();
    }
/*


    public void filterlist(ArrayList<Instructor> filterlist) {
        items = filterlist;
        Log.i("items",""+items);
        notifyDataSetChanged();
    }
*/

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        // creating variables for our text views.
        private final TextView name,lang,rating,distance;
        ImageView img_icon;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing our text views.
            name = itemView.findViewById(R.id.tv_ins_name);
            lang=itemView.findViewById(R.id.tv_ins_lang);
            img_icon= itemView.findViewById(R.id.ivProfile);
            rating=itemView.findViewById(R.id.tv_rate);
            distance=itemView.findViewById(R.id.tv_ins_distance);
        }
        }
        }




