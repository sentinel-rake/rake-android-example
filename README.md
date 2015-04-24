
## Rake Android Example

[Rake-Android](https://github.com/skpdi/rake-android) 의 예제입니다. **Rake API** 등 더 자세한 내용은 [Rake Document: Android](https://github.com/skpdi/rake-document/wiki/1.-Rake-Android) 를 참조해주세요.

- `MainActivity` 는 `app/src/main/java/com/skp/di/sentinel/rake_android_example` 에 있습니다.
- **Rake** 및 **Sentinel-Shuttle** 라이브러리는 `app/libs` 에 위치해 있습니다.
- 테스트용으로 사용하실려면, 먼저 `token` 을 세팅해주세요.


### Code

```java
public class MainActivity extends ActionBarActivity {
    // Important: set your rake token
    private final static String rakeToken = "your rake token";
    private final static Boolean willFlushIntoDevServer = true; // is dev environment or not?
    private RakeAPI rake = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialize Rake instance
        RakeAPI.setDebug(true); // print debug messages. Default: false

        /*
            arg1: this context
            arg2: rake token which are generated by Sentinel
            arg3: if this argument is true, rake will flush log into dev server,
                  otherwise log will be sent to live server.
                  Also if this argument is true, every log will be sent to dev server instantly
                  without saving into SQLite
         */
        rake = RakeAPI.getInstance(this, rakeToken, willFlushIntoDevServer);


        // button to track a log.
        Button btnTrack = (Button) findViewById(R.id.btnTrack);
        btnTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // initialize shuttle
                AppSampleSentinelShuttle shuttle = new AppSampleSentinelShuttle();

                // shuttle provides a logging method per action.
                // let action name is 'action4'
                shuttle.setBodyOfaction4("field1 value", "field3 value", "field4 value");

                // track a log. log will be saved into SQLite (local storage)
                Log.d("shuttle string", shuttle.toJSONString());
                rake.track(shuttle.toJSONObject());

                // if you need to send a log immediately, flush a log after tracking
                // rake.flush();
            }
        });

        // button to flush log.
        Button btnFlush = (Button) findViewById(R.id.btnFlush);
        btnFlush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rake.flush();
            }
        });
    }

    @Override
    protected void onStop() {
        // you can flush tracked logs in onStop() according to your flushing policy
        rake.flush();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        // you must flush tracked logs in onDestroy().
        rake.flush();
        super.onDestroy();
    }

    ...
}
```

### Screenshot

![](https://raw.githubusercontent.com/skpdi/rake-android-example/master/screenshots/image01.jpeg)
