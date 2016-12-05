/*
 * Copyright 2016 Shakuganns.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package me.imirai.library;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.RelativeSizeSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Shakugan on 2016/11/17.
 */

public class KeywordTextView extends TextView {

    private String keywords;
    private int keywordsColor;
    private int urlColor;
    private int numColor;
    private boolean keywordsUnderline;
    private boolean urlUnderline;
    private boolean numUnderline;
    private boolean urlHighlight;
    private boolean numHighlight;
    private float keywordsRelativeSize;
    private float urlRelativeSize;
    private float numRelativeSize;

    private SpannableString spannableString;

    private static String URL_REGEX = "(((http|ftp|https)://)|(www\\.))[a-zA-Z0-9\\._-]+\\.[a-zA-Z]{2,6}(:[0-9]{1,4})?(/[a-zA-Z0-9\\&%_\\./-~-]*)?";
    private Pattern urlPattern = Pattern.compile(URL_REGEX);

    private static String NUM_REGEX = "\\d{6,18}";
    private Pattern numPattern = Pattern.compile(NUM_REGEX);

    private OnKeywordClickListener listener;
    private OnUrlClickListener onUrlClickListener;

    public KeywordTextView(Context context) {
        this(context,null);
    }

    public KeywordTextView(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }

    public KeywordTextView(
            Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs,defStyleAttr);
    }

    private void init(Context context,AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs,R.styleable.KeywordTextView,defStyleAttr,0);
        keywords = typedArray.getString(R.styleable.KeywordTextView_keyword);

        keywordsColor = typedArray.getColor(R.styleable.KeywordTextView_keyword_color,Color.BLUE);
        urlColor = typedArray.getColor(R.styleable.KeywordTextView_url_color,Color.BLUE);
        numColor = typedArray.getColor(R.styleable.KeywordTextView_num_color,Color.BLUE);

        keywordsUnderline = typedArray.getBoolean(R.styleable.KeywordTextView_keywords_underline,true);
        urlUnderline = typedArray.getBoolean(R.styleable.KeywordTextView_url_underline,true);
        numUnderline = typedArray.getBoolean(R.styleable.KeywordTextView_num_underline,true);

        urlHighlight = typedArray.getBoolean(R.styleable.KeywordTextView_url_highlight,false);
        numHighlight = typedArray.getBoolean(R.styleable.KeywordTextView_num_highlight,false);

        keywordsRelativeSize = typedArray.getFloat(R.styleable.KeywordTextView_keywords_relativeSize,1.0f);
        urlRelativeSize = typedArray.getFloat(R.styleable.KeywordTextView_url_relativeSize,1.0f);
        numRelativeSize = typedArray.getFloat(R.styleable.KeywordTextView_num_relativeSize,1.0f);

        typedArray.recycle();
        setTextWithKeyword(getText(),keywords);
        setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void findNum() {
        String flagString = spannableString.toString();
        Matcher matcher = numPattern.matcher(spannableString);
        boolean find = matcher.find();
        while (find) {
            String s = "";
            for (int i = 0 ; i < matcher.group().length() ; i++) {
                s = s+"^";
            }
            flagString = flagString.replaceFirst(matcher.group(),s);
            Log.i("flagString",flagString);
            String text = spannableString.toString();
            spannableString.setSpan(new RelativeSizeSpan(numRelativeSize), flagString.indexOf(s),
                    flagString.indexOf(s) + s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View widget) {

                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setColor(numColor);
                    ds.setUnderlineText(numUnderline);
                }
            }, text.indexOf(matcher.group()), text.indexOf(matcher.group()) + matcher.group().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            matcher = numPattern.matcher(flagString);
            find = matcher.find();
        }
    }

    /**
     * 在文本中找出url并且高亮显示
     */

    private void findUrl() {
        String flagString = spannableString.toString();
        Matcher matcher = urlPattern.matcher(spannableString);
        boolean find = matcher.find();
        while (find) {
            String s = "";
            final String url = matcher.group();
            for (int i = 0 ; i < matcher.group().length() ; i++) {
                s = s+"^";
            }
            flagString = flagString.replaceFirst(matcher.group(),s);
            String text = spannableString.toString();
            spannableString.setSpan(new RelativeSizeSpan(urlRelativeSize), flagString.indexOf(s),
                    flagString.indexOf(s) + s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    if (onUrlClickListener != null) {
                        onUrlClickListener.onClick(widget, url);
                    } else {
                        Intent intent = new Intent();
                        intent.setAction("android.intent.action.VIEW");
                        Uri uri = Uri.parse(url);
                        intent.setData(uri);
                        widget.getContext().startActivity(intent);
                    }
                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setColor(urlColor);
                    ds.setUnderlineText(urlUnderline);
                }
            }, text.indexOf(matcher.group()), text.indexOf(matcher.group()) + matcher.group().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            matcher = urlPattern.matcher(flagString);
            find = matcher.find();
        }
    }

    /**
     *
     * @param text 显示的文本
     * @param keyword 需要添加超链接的关键字;多个关键字用,隔开  如："fuck,shit,……"
     */

    public void setTextWithKeyword(CharSequence text, final CharSequence keyword) {
        if (text == null) {
            return;
        } else {
            spannableString = new SpannableString(text);
        }
        if (!TextUtils.isEmpty(keyword)) {
            String[] values = keyword.toString().split(",");
            String flagString = text.toString();
            for (final String s:values) {
                String flag = "";
                for (int i = 0 ; i < s.length() ; i++) {
                    flag = flag+"^";
                }
                while(flagString.contains(s)) {
                    spannableString.setSpan(new RelativeSizeSpan(keywordsRelativeSize), flagString.indexOf(s),
                            flagString.indexOf(s) + s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spannableString.setSpan(new ClickableSpan() {
                        @Override
                        public void onClick(View widget) {
                            if (listener != null) {
                                listener.onClick(widget , s);
                            }
                        }

                        @Override
                        public void updateDrawState(TextPaint ds) {
                            super.updateDrawState(ds);
                            ds.setColor(keywordsColor);
                            ds.setUnderlineText(keywordsUnderline);
                        }
                    }, flagString.indexOf(s), flagString.indexOf(s) + s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    flagString = flagString.replaceFirst(s,flag);
                }
            }
        }
        if (urlHighlight) {
            findUrl();
        }
        if (numHighlight) {
            findNum();
        }
        setText(spannableString);
    }

    public SpannableString getSpannableText() {
        return spannableString;
    }

    public int getKeywordsColor() {
        return keywordsColor;
    }

    public int getNumColor() {
        return numColor;
    }

    public int getUrlColor() {
        return urlColor;
    }

    public String getKeywords() {
        return keywords;
    }

    public float getKeywordsRelativeSize() {
        return keywordsRelativeSize;
    }

    public float getNumRelativeSize() {
        return numRelativeSize;
    }

    public float getUrlRelativeSize() {
        return urlRelativeSize;
    }

    public void setOnKeywordClickListener(OnKeywordClickListener listener) {
        this.listener = listener;
    }

    public void setOnUrlClickListener(OnUrlClickListener onUrlClickListener) {
        this.onUrlClickListener = onUrlClickListener;
    }

    public interface OnKeywordClickListener {

        void onClick(View view,String keyword);

    }

    public interface OnUrlClickListener {

        void onClick(View view,String url);

    }
}
