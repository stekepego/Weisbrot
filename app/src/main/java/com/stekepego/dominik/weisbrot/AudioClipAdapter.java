package com.stekepego.dominik.weisbrot;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Dominik on 29.03.2016.
 */
public class AudioClipAdapter extends ArrayAdapter<AudioClip>{
    Context context;
    int layoutResourceId;
    List<AudioClip> data = null;

    public AudioClipAdapter(Context context, int layoutResourceId, List<AudioClip> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        View row = convertView;
        ClipHolder holder;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new ClipHolder();

            holder.category = (TextView)row.findViewById(R.id.clip_category);
            holder.clipName = (TextView)row.findViewById(R.id.clip_title);

            row.setTag(holder);
        }
        else{
            holder = (ClipHolder)row.getTag();
        }

        final AudioClip audioClip = data.get(position);
        holder.clipName.setText(audioClip.clipName);
        holder.category.setText(audioClip.category);

        final View bgView = row;

        row.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final MediaPlayer mediaPlayer;
                int resId = context.getResources().getIdentifier(audioClip.fileName, "raw", context.getPackageName());

                mediaPlayer = MediaPlayer.create(context, resId);
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mediaPlayer.release();
                    }
                });
                mediaPlayer.start();
            }
        });

        return row;
    }

    static class ClipHolder
    {
        TextView clipName;
        TextView category;
    }
}
