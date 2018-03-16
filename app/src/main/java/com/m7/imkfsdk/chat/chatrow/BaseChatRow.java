package com.m7.imkfsdk.chat.chatrow;

import android.content.Context;
import android.view.ContextMenu;
import android.view.View;

import com.bumptech.glide.Glide;
import com.odbpo.fenggou.im_rlqm_kf.R;
import com.m7.imkfsdk.chat.holder.BaseHolder;
import com.m7.imkfsdk.chat.holder.ViewHolderTag;
import com.moor.imkf.model.entity.FromToMessage;

/**
 * Created by longwei on 2016/3/9.
 * 处理基本的姓名和头像显示，消息发送状态，这些都是相同的
 */
public abstract class BaseChatRow implements IChatRow {

    int mRowType;

    public BaseChatRow(int type) {
        mRowType = type;
    }

    /**
     * 处理消息的发送状态设置
     *
     * @param position 消息的列表所在位置
     * @param holder   消息ViewHolder
     * @param l
     */
    protected static void getMsgStateResId(int position, BaseHolder holder, FromToMessage msg, View.OnClickListener l) {
        if (msg != null && msg.userType.equals("0")) {
            String msgStatus = msg.sendState;
            if (msgStatus.equals("false")) {
                holder.getUploadState().setImageResource(R.drawable.kf_chat_failure_msgs);
                holder.getUploadState().setVisibility(View.VISIBLE);
                if (holder.getUploadProgressBar() != null) {
                    holder.getUploadProgressBar().setVisibility(View.GONE);
                }
            } else if (msgStatus.equals("true")) {
                holder.getUploadState().setImageResource(0);
                holder.getUploadState().setVisibility(View.GONE);
                if (holder.getUploadProgressBar() != null) {
                    holder.getUploadProgressBar().setVisibility(View.GONE);
                }

            } else if (msgStatus.equals("sending")) {
                holder.getUploadState().setImageResource(0);
                holder.getUploadState().setVisibility(View.GONE);
                if (holder.getUploadProgressBar() != null) {
                    holder.getUploadProgressBar().setVisibility(View.VISIBLE);
                }

            } else {
                if (holder.getUploadProgressBar() != null) {
                    holder.getUploadProgressBar().setVisibility(View.GONE);
                }
            }

            ViewHolderTag holderTag = ViewHolderTag.createTag(msg, ViewHolderTag.TagType.TAG_RESEND_MSG, position);
            holder.getUploadState().setTag(holderTag);
            holder.getUploadState().setOnClickListener(l);
        }
    }

    /**
     * @param contextMenu
     * @param targetView
     * @param detail
     * @return
     */
    public abstract boolean onCreateRowContextMenu(ContextMenu contextMenu, View targetView, FromToMessage detail);

    /**
     * 填充数据
     *
     * @param context
     * @param baseHolder
     * @param detail
     * @param position
     */
    protected abstract void buildChattingData(Context context, BaseHolder baseHolder, FromToMessage detail, int position);

    @Override
    public void buildChattingBaseData(Context context, BaseHolder baseHolder, FromToMessage detail, int position) {

        // 处理其他逻辑
        buildChattingData(context, baseHolder, detail, position);

        //设置用户头像
        String imgPath = detail.im_icon;
        if (baseHolder.getChattingAvatar() != null) {
            if ("1".equals(detail.userType)) {

                if (detail.showHtml != null && "true".equals(detail.showHtml)) {
                    //机器人头像
                    Glide.with(context).load(R.drawable.kf_head_default_robot).into(baseHolder.getChattingAvatar());
                } else if (imgPath != null && !"".equals(imgPath)) {
                    //客服头像
                    Glide.with(context).load(imgPath + "?imageView2/0/w/100/h/100")
                            .placeholder(R.drawable.kf_head_default_local)
                            .into(baseHolder.getChattingAvatar());
                }
            }

        }
    }


}
