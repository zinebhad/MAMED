package com.example.pfe.Fragments;
import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.pfe.Adapters.BookAdapter;
import com.example.pfe.Models.Book;
import com.example.pfe.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    RecyclerView bookRecyclerView ;
    BookAdapter bookAdapter ;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference ;
    public List<Book> bookList;
    EditText editText;
    private RadioGroup SearchBy;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;



    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @SuppressLint("WrongConstant")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_home, container, false);
        editText=fragmentView.findViewById(R.id.edit);
        SearchBy=fragmentView.findViewById(R.id.Searche_by);
        firebaseAuth= FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        bookRecyclerView  = fragmentView.findViewById(R.id.postRV);
        bookRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        bookRecyclerView.setHasFixedSize(true);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Books");
        if (firebaseUser==null){
            SearchBy.setVisibility(4);
            SearchBy.removeAllViews();
           RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) bookRecyclerView.getLayoutParams();
            lp.setMargins(0,0,0,0);
        }
        else {
            SearchBy.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @SuppressLint("WrongConstant")
                public void onCheckedChanged(RadioGroup group, final int checkedId) {
                    editText.setVisibility(0);
                    switch (checkedId) {
                        case R.id.S_author:
                            SearchByAuthor();
                            break;
                        case R.id.S_Title:
                            SearchByTitle();
                            break;
                    }


                    //}
                }
            });
        }

            return fragmentView ;
        }
        public void SearchByTitle(){
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    filterT(editable.toString());

                }
            });
        }
        public void SearchByAuthor(){
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }
                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }
                @Override
                public void afterTextChanged(Editable editable) {
                    filterA(editable.toString());
                }
            });
        }

        public void filterT(String text){
            ArrayList<Book> newList = new ArrayList<>();
            for (Book b : bookList) {
                if (b.getTitle().toLowerCase().contains(text)) {
                    newList.add(b);
                }
            }
            bookAdapter.updateList(newList);
        }
        public void filterA(String text){
            ArrayList<Book> newList = new ArrayList<>();
            for (Book b : bookList) {
                if (b.getAuthor().toLowerCase().contains(text)) {
                    newList.add(b);
                }
            }
            bookAdapter.updateList(newList);
        }
    @Override
    public void onStart() {
        super.onStart();

        // Get List Posts from the database

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                bookList = new ArrayList<>();
                for (DataSnapshot booksnap: dataSnapshot.getChildren()) {

                    Book book = booksnap.getValue(Book.class);
                    bookList.add(book) ;
                }

                bookAdapter = new BookAdapter(getActivity(),bookList);
                bookRecyclerView.setAdapter(bookAdapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        firebaseDatabase.getReference("Rating").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i=0;
                float s=0,m=0;
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    i=0;s=0;m=0;
                    for (DataSnapshot snapshot:snap.getChildren()){
                        i +=1;
                        s +=Float.valueOf(snapshot.getValue().toString());
                    }
                    m= (s/i);
                    firebaseDatabase.getReference("Books").child(snap.getKey()).child("Score").setValue(m);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}