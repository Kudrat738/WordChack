package com.example.wordcheck.util;

import android.content.Context;
import android.example.wordcheck.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.wordcheck.kind.Vocabulary;

import java.util.List;

/**
 * Created by 此文件打不开 on 2020/3/31.
 */

public class VocabularyAdapter extends ArrayAdapter<Vocabulary>  {
    private int resourceId;
   // private ImageView delete;
   // WordsAction wordsAction;
    Vocabulary vocabulary;
   // Context context;
    public VocabularyAdapter(Context context, int textViewResourceId, List<Vocabulary> objects){
        super(context,textViewResourceId,objects);
      //  wordsAction = WordsAction.getInstance(context);
        resourceId=textViewResourceId;
    }
    @Override
    public View getView( final int position, View converView, ViewGroup parent){
        Vocabulary vocabulary=getItem(position);
        View view;
        ViewHolder viewHolder;

        if (converView==null){
            view= LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
            viewHolder=new ViewHolder();
            viewHolder.wordKey=(TextView) view.findViewById(R.id.wordsKey);
            viewHolder.translation=(TextView) view.findViewById(R.id.translation);
            viewHolder.masteryLevel=(TextView)view.findViewById(R.id.grasp);
            viewHolder.right=(TextView)view.findViewById(R.id.right);
            viewHolder.wrong=(TextView)view.findViewById(R.id.wrong);

         //   viewHolder.voiceE=(Button)view.findViewById(R.id.voice_e);
          //  viewHolder.voiceA=(Button)view.findViewById(R.id.voice_a);
          // viewHolder.voiceA.setTag(position);
           // viewHolder.voiceE.setTag(position);
       //    viewHolder.delete.setTag(position);
            view.setTag(viewHolder);
        }else {
            view=converView;
            viewHolder=(ViewHolder) view.getTag();
           /* viewHolder.delete=(ImageView) view.findViewById(R.id.delete);
            delete.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    Log.d("测试", "副");
                }
            });*/
           // delete.setOnClickListener(this);
//           delete.setTag(position);

        }

        viewHolder.translation.setText(vocabulary.getTranslation());
        viewHolder.wordKey.setText(vocabulary.getWordsKey());
        viewHolder.masteryLevel.setText(String.valueOf(vocabulary.getMasteryLevel()));
        viewHolder.right.setText(String.valueOf(vocabulary.getRight()));
        viewHolder.wrong.setText(String.valueOf(vocabulary.getWrong()));
       // viewHolder.delete.setTag(position);
      //  viewHolder.delete.setOnClickListener(myListener);
   //     viewHolder.voiceA.setOnClickListener(myListener);
  //      viewHolder.voiceE.setOnClickListener(myListener);


//        masteryLevel.setText(vocabulary.getMasteryLevel());
   //     right.setText(vocabulary.getRight());
   //     wrong.setText(vocabulary.getWrong());

        return view;
    }

  /*  @Override
    public void onClick(View v) {
        ImageView imageView=(ImageView) v;
        Context context= AppContext.getContext();
        Log.d("测试", "副");
        wordsAction.playMP3(vocabulary.getWordsKey(), "A",context );

    }*/

    class ViewHolder{
        TextView wordKey,translation,masteryLevel,right,wrong;
        //ImageView delete;
    }


}
