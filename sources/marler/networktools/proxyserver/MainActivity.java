package marler.networktools.proxyserver;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;
import java.io.IOException;
import java.net.Socket;
import marler.networktools.HttpProxyListener;
import marler.networktools.NamedWorkerThread;
import marler.networktools.ServerCallback;

public class MainActivity extends Activity implements OnClickListener, ServerCallback {
    private HttpProxyListener currentListener = null;
    private final Handler mainLoopHandler = new Handler(Looper.getMainLooper());
    private boolean printDebug;
    private TextView serverLogTextView = null;
    private ToggleButton toggleServerButton = null;

    /* renamed from: marler.networktools.proxyserver.MainActivity$1 */
    class C00371 implements OnClickListener {

        /* renamed from: marler.networktools.proxyserver.MainActivity$1$1 */
        class C00361 implements Runnable {
            C00361() {
            }

            public void run() {
                MainActivity.this.serverLogTextView.setText("");
            }
        }

        C00371() {
        }

        public void onClick(View v) {
            MainActivity.this.mainLoopHandler.post(new C00361());
        }
    }

    /* renamed from: marler.networktools.proxyserver.MainActivity$2 */
    class C00382 implements OnClickListener {
        C00382() {
        }

        public void onClick(View v) {
            MainActivity.this.startActivity(new Intent(MainActivity.this.getBaseContext(), SettingsActivity.class));
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0040R.layout.activity_main);
        this.serverLogTextView = (TextView) findViewById(C0040R.id.serverLogText);
        this.serverLogTextView.setBackgroundColor(-1);
        this.toggleServerButton = (ToggleButton) findViewById(C0040R.id.toggleServerButton);
        this.toggleServerButton.setOnClickListener(this);
        ((Button) findViewById(C0040R.id.clearLogButton)).setOnClickListener(new C00371());
        ((Button) findViewById(C0040R.id.settingsButton)).setOnClickListener(new C00382());
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(C0040R.menu.activity_main, menu);
        return true;
    }

    private void println(final String msg) {
        System.out.println(msg);
        this.mainLoopHandler.post(new Runnable() {
            public void run() {
                MainActivity.this.serverLogTextView.append(msg + "\n");
            }
        });
    }

    public void onClick(View view) {
        HttpProxyListener listener = this.currentListener;
        if (((ToggleButton) view).isChecked()) {
            if (listener == null) {
                int httpProxyListenPort = 8080;
                try {
                    httpProxyListenPort = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getString("httpProxyListenPort", "8080"));
                } catch (NumberFormatException e) {
                }
                println(String.format("Starting proxy listener on port %d...", new Object[]{Integer.valueOf(httpProxyListenPort)}));
                this.currentListener = new HttpProxyListener(httpProxyListenPort, this);
                new Thread(this.currentListener).start();
            }
        } else if (listener != null) {
            this.currentListener = null;
            println("Stopping proxy listener...");
            listener.stop();
        }
    }

    public void ioException(NamedWorkerThread workerThread, Socket client, IOException e) {
        println(String.format("[%s] IOException: %s", new Object[]{workerThread.getWorkerName(), e.getMessage()}));
    }

    public void ioException(NamedWorkerThread workerThread, Socket socketA, Socket socketB, IOException e) {
        println(String.format("[%s] IOException: %s", new Object[]{workerThread.getWorkerName(), e.getMessage()}));
    }

    public void closed(NamedWorkerThread workerThread, Socket client) {
        println(String.format("[%s] Closed", new Object[]{workerThread.getWorkerName()}));
    }

    public void clientSentInvalidData(NamedWorkerThread workerThread, Socket client, String errorMessage) {
        println(String.format("[%s] Error: %s", new Object[]{workerThread.getWorkerName(), errorMessage}));
    }

    public void error(NamedWorkerThread workerThread, Socket client, String errorMessage) {
        println(String.format("[%s] Error: %s", new Object[]{workerThread.getWorkerName(), errorMessage}));
    }

    public void log(NamedWorkerThread workerThread, String logMessage) {
        println(String.format("[%s] LOG: %s", new Object[]{workerThread.getWorkerName(), logMessage}));
    }

    public void debug(NamedWorkerThread workerThread, String debugMessage) {
        if (this.printDebug) {
            println(String.format("[%s] DEBUG: %s", new Object[]{workerThread.getWorkerName(), debugMessage}));
        }
    }
}
