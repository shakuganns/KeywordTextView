package me.imirai.keywordtextview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import me.imirai.library.KeywordTextView;

public class MainActivity extends AppCompatActivity {

    private KeywordTextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (KeywordTextView) findViewById(R.id.text);
        textView.setOnKeywordClickListener(new KeywordTextView.OnKeywordClickListener() {
            @Override
            public void onClick(View view, String keyword) {
                Toast.makeText(MainActivity.this,
                        keyword,Toast.LENGTH_SHORT).show();
            }
        });
        textView.setTextWithKeyword("A pretty girl! So pretty！https://www.google.com 0000000","pretty,girl");
    }
}
