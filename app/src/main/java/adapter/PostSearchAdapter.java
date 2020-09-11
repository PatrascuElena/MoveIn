package adapter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.movein.PostSearchActivity;
import com.movein.ProfileActivity;
import com.movein.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import model.PostModel;

public class PostSearchAdapter extends RecyclerView.Adapter<PostSearchAdapter.ViewHolder> {
    Context context;
    List<PostModel> posts;


    public PostSearchAdapter(Context context, List<PostModel> posts) {
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post_search, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final PostModel post = posts.get(position);
        holder.postTitle.setText(post.getPost());
        holder.postadresa.setText(post.getAdresa());
        holder.postdescr.setText(post.getDescriere());
        holder.postoras.setText(post.getOras());
        holder.postpret.setText(post.getPret());
        holder.postsupraf.setText(post.getSuprafata());
        holder.callbutton.setText(post.getContact());
        holder.callbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nr = holder.callbutton.getText().toString();
                Intent callIntent =new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"+nr));
                context.startActivity(callIntent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.post_title)
        TextView postTitle;
        @BindView(R.id.postdescr)
        TextView postdescr;
        @BindView(R.id.postoras)
        TextView postoras;
        @BindView(R.id.postsupraf)
        TextView postsupraf;
        @BindView(R.id.postadresa)
        TextView postadresa;
        @BindView(R.id.postpret)
        TextView postpret;
        @BindView(R.id.callbutton)
        Button callbutton;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
