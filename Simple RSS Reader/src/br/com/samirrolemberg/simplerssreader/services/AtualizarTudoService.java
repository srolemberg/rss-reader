package br.com.samirrolemberg.simplerssreader.services;

import java.util.ArrayList;
import java.util.LinkedList;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import br.com.samirrolemberg.simplerssreader.model.Feed;
import br.com.samirrolemberg.simplerssreader.tasks.AtualizarFeedsTask;

public class AtualizarTudoService extends Service {

	//private NotificationManager mNotifyManager = null;
	//private NotificationCompat.Builder mBuilder = null;
	private Context context = null;
	//public int id = 15;
	private AtualizarFeedsTask task;

	private ArrayList<Feed> listaFeeds;
	
	//AQUI ELE INICIA UMA THREAD E ENFILEIRA ELA POR CHAMADA.
	//É INTERESSANTE NÃO 
	public AtualizarTudoService() {
		super();
		this.context = this;
		//this.id = new Random().nextInt(999);//colocar parametero
		Log.w("MY-SERVICES", "new AtualizarTudoService");		
	}

	@Override
	public void onCreate() {
		super.onCreate();//NAO OMITIR ESTE FILHO DA PUTA AO FAZER UM INTENTSERVICE!	
	};

	@Override
	public void onDestroy() {
		super.onDestroy();
		task.cancel(false);
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.w("MY-SERVICES", "Atualizar Tudo Service Start");
		listaFeeds = (ArrayList<Feed>) intent.getExtras().get("Feeds");
		String[] args = new String[listaFeeds.size()];
		LinkedList<String> rss = new LinkedList<String>();
		for (int i = 0; i < listaFeeds.size(); i++) {
			rss.add(listaFeeds.get(i).getRss());
		}
		args = rss.toArray(args);
		task = new AtualizarFeedsTask(context, this, intent);
		task.execute(args);//executa a tarefa
		//return super.onStartCommand(intent, flags, startId);
		return START_REDELIVER_INTENT;
	}
}

