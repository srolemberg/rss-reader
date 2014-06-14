package br.com.samirrolemberg.simplerssreader.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import br.com.samirrolemberg.simplerssreader.model.Feed;
import br.com.samirrolemberg.simplerssreader.tasks.LimparConteudoFeedTask;

public class LimparConteudoService extends Service {

	//private NotificationManager mNotifyManager = null;
	//private NotificationCompat.Builder mBuilder = null;
	private Context context = null;
	//public int id = 15;
	private LimparConteudoFeedTask task;

	private Feed feedAux;
	//private long idFeed;
	
	//AQUI ELE INICIA UMA THREAD E ENFILEIRA ELA POR CHAMADA.
	//É INTERESSANTE NÃO 
	public LimparConteudoService() {
		super();
		this.context = this;
		//this.id = new Random().nextInt(999);//colocar parametero
		Log.w("MY-SERVICES", "new LimparConteudoService");		
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
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.w("MY-SERVICES", "Limpar Conteudo Service Start");
		feedAux = (Feed) intent.getExtras().get("Feed");
		//idFeed = ((Long) intent.getExtras().get("idFeed")).longValue();
		
		//repassa o id do novo feed vazio que sera exibido na outra tela! não poderá acessar até que a task mude a flag
		task = new LimparConteudoFeedTask(context, this, intent, feedAux);
		task.execute();//executa a tarefa
		//return super.onStartCommand(intent, flags, startId);
		return START_REDELIVER_INTENT;
	}
}

