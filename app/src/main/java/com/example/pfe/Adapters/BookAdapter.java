package com.example.pfe.Adapters;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.pfe.Activities.BookDetailActivity;
import com.example.pfe.Models.Book;
import com.example.pfe.R;
import java.util.List;
public class BookAdapter extends RecyclerView.Adapter<BookAdapter.MyViewHolder> {
    Context mContext;
    List<Book> mData ;
    public BookAdapter(Context mContext, List<Book> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }
    public void updateList(List<Book> newList){
        mData=newList;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(mContext).inflate(R.layout.row_book_item,parent,false);
        return new MyViewHolder(row);
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tvTitle.setText(mData.get(position).getTitle());
        holder.ratingBar.setRating(mData.get(position).getScore());
        holder.Author.setText(mData.get(position).getAuthor());
        Glide.with(mContext).load(mData.get(position).getPicture()).into(holder.imgBook);
        Glide.with(mContext).load(mData.get(position).getUserPhoto()).into(holder.imgBookProfile);
    }
    @Override
    public int getItemCount() {
        return mData.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle,Author;
        ImageView imgBook;
        ImageView imgBookProfile;
        RatingBar ratingBar;
        public MyViewHolder(View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.row_book_title);
            Author=itemView.findViewById(R.id.row_book_Author);
            imgBook = itemView.findViewById(R.id.row_book_img);
            imgBookProfile = itemView.findViewById(R.id.row_book_profile_img);
            ratingBar=(RatingBar)itemView.findViewById(R.id.ratebar);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent BookDetailActivity = new Intent(mContext, BookDetailActivity.class);
                    int position = getAdapterPosition();
                    BookDetailActivity.putExtra("bookImage",mData.get(position).getPicture());
                    BookDetailActivity.putExtra("title",mData.get(position).getTitle());
                    BookDetailActivity.putExtra("bookKey",mData.get(position).getBookKey());
                    BookDetailActivity.putExtra("userPhoto",mData.get(position).getUserPhoto());
                    long timestamp  = (long) mData.get(position).getTimeStamp();
                    BookDetailActivity.putExtra("bookDate",timestamp) ;
                    mContext.startActivity(BookDetailActivity);
                }
            });
        }
    }
}