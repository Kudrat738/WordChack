package com.example.wordcheck.list;

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
    public VocabularyAdapter(Context context, int textViewResourceId, List<Vocabulary> objects){
        super(context,textViewResourceId,objects);
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
            //设置一个额外的数据
            view.setTag(viewHolder);
        }else {
            view=converView;
            //得到额外的数据
            viewHolder=(ViewHolder) view.getTag();
        }

        viewHolder.translation.setText(vocabulary.getTranslation());
        viewHolder.wordKey.setText(vocabulary.getWordsKey());
        viewHolder.masteryLevel.setText(String.valueOf(vocabulary.getMasteryLevel()));
        viewHolder.right.setText(String.valueOf(vocabulary.getRight()));
        viewHolder.wrong.setText(String.valueOf(vocabulary.getWrong()));
        return view;
    }



    class ViewHolder{
        TextView wordKey,translation,masteryLevel,right,wrong;

    }


}
