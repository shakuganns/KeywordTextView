# KeywordTextView
A Textview that allows you to highlight the keyword.

## Screenshot
[![screen](https://github.com/shakuganns/KeywordTextView/blob/master/art/ss1.png)](https://github.com/shakuganns/KeywordTextView)

URL and 6 to 18 digit numbers have the same effect.

[![screen](https://github.com/shakuganns/KeywordTextView/blob/master/art/ss2.png)](https://github.com/shakuganns/KeywordTextView)

## Usage
* Gradle
```
compile 'me.imirai:keywordtextview:0.1.0'
```
* Maven
```
<dependency>
  <groupId>me.imirai</groupId>
  <artifactId>keywordtextview</artifactId>
  <version>0.1.0</version>
  <type>arr</type>
</dependency> 
```

## Sample
* Xml
```
<me.imirai.library.KeywordTextView
        app:keyword="hello,world"
        app:keyword_color="@color/colorPrimary"
        app:has_underline="true"
        android:id="@+id/text"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Hello World!" />
```

* Java
```
textView.setTextWithKeyword("A pretty girl! So pretty！","pretty,girl");

//You can do what you want to do by setting up the listener.
textView.setOnKeywordClickListener(new KeywordTextView.OnKeywordClickListener() {
            @Override
            public void onClick(View view, String keyword) {
                Toast.makeText(MainActivity.this,
                        keyword,Toast.LENGTH_SHORT).show();
            }
        });
```

License
--------

Copyright 2016 Shakugan.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
