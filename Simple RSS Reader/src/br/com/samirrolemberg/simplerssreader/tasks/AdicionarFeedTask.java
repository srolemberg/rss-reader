package br.com.samirrolemberg.simplerssreader.tasks;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import br.com.samirrolemberg.simplerssreader.AdicionarFeedActivity;
import br.com.samirrolemberg.simplerssreader.R;
import br.com.samirrolemberg.simplerssreader.model.ExceptionMessage;
import br.com.samirrolemberg.simplerssreader.model.Feed;
import br.com.samirrolemberg.simplerssreader.model.SimpleFeed;
import br.com.samirrolemberg.simplerssreader.tasks.notification.SalvarNovoFeedTask;

import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndFeed;
import com.google.code.rome.android.repackaged.com.sun.syndication.io.FeedException;
import com.google.code.rome.android.repackaged.com.sun.syndication.io.SyndFeedInput;

public class AdicionarFeedTask extends AsyncTask<String, Integer, Feed> {

	private final Context context;
	private ProgressDialog progress;

	private SyndFeed feed;
	private ExceptionMessage e;
	
	public AdicionarFeedTask(Context context){
		this.context = context;
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		progress = ProgressDialog.show(getContext(), "Adicionar Feed", "Sincronizando FEED...",true,true);
	}
	@Override
	protected Feed doInBackground(String... arg) {
		try {
			URL feedUrl = new URL(arg[0]);
			SyndFeedInput input = new SyndFeedInput();
			this.feed = input.build(new InputStreamReader(feedUrl.openStream()));
			if (feed != null) {
				return SimpleFeed.consumir(feed, arg[0]);
//				Feed f = SimpleFeed.consumir(feed, arg[0]);
//				return f;
			}
		} catch (MalformedURLException e) {//problema ao acessar url, verifique a digita��o! protocolo nao encontrado.
			Log.e("ADDFEED", "MalformedURLException");
			Log.e("ADDFEED", e.getMessage(),e);
			Log.e("ADDFEED", e.getLocalizedMessage(),e);
			this.e = new ExceptionMessage(e);
		} catch (IllegalArgumentException e) {
			Log.e("ADDFEED", "IllegalArgumentException");
			Log.e("ADDFEED", e.getMessage(),e);
			Log.e("ADDFEED", e.getLocalizedMessage(),e);
			this.e = new ExceptionMessage(e);
		} catch (FeedException e) {//XMl invpalido - nenhum elemento encontrado
			Log.e("ADDFEED", "FeedException");
			Log.e("ADDFEED", e.getMessage(),e);
			Log.e("ADDFEED", e.getLocalizedMessage(),e);
			this.e = new ExceptionMessage(e);
		} catch (IOException e) {//arquivo n�o encontrado
			Log.e("ADDFEED", "IOException");
			Log.e("ADDFEED", e.getMessage(),e);
			Log.e("ADDFEED", e.getLocalizedMessage(),e);
			this.e = new ExceptionMessage(e);
		} catch (Exception e) {
			Log.e("ADDFEED", "Exception");
			Log.e("ADDFEED", e.getMessage(),e);
			Log.e("ADDFEED", e.getLocalizedMessage(),e);
			this.e = new ExceptionMessage(e);
		}
		return null;
	}

	@Override
	protected void onPostExecute(final Feed result) {
		super.onPostExecute(result);
		progress.dismiss();
		if (result!=null) {
			//Log.w("OUTPUT-TEST", result.toString());
			
			final AdicionarFeedActivity activity = (AdicionarFeedActivity) getContext();
			LinearLayout layout = (LinearLayout) activity.findViewById(R.id.dados_feed__adicionar_feed_activity);
			layout.setVisibility(View.VISIBLE);

			TextView nome 		= (TextView) activity.findViewById(R.id.nome__adicionar_feed_activity);
			TextView descricao 	= (TextView) activity.findViewById(R.id.descricao__adicionar_feed_activity);
			TextView link 		= (TextView) activity.findViewById(R.id.link__adicionar_feed_activity);
			TextView idioma 	= (TextView) activity.findViewById(R.id.idioma__adicionar_feed_activity);
			TextView categoria 	= (TextView) activity.findViewById(R.id.categoria__adicionar_feed_activity);
			Button adicionar 	= (Button)	 activity.findViewById(R.id.adicionar__adicionar_feed_activity);

			nome.setText(result.getTitulo());
			descricao.setText(result.getDescricao());
			link.setText(result.getLink());
			idioma.setText(result.getIdioma());
			categoria.setText(result.getCategorias().toString());
						

			adicionar.setOnClickListener(new OnClickListener() {				
				@Override
				public void onClick(View v) {
					SalvarNovoFeedTask task = new SalvarNovoFeedTask(getContext(), result);
					String[] params = {""};
					task.execute(params);
					//NavUtils.navigateUpFromSameTask(activity);
					activity.onBackPressed();
					//TODO: !1 USAR O NAVUTILS NO FUTURO E COLOCOCAR UMA FLAGNO BANCO PARA NÃO EXIBIR O FEED QUE AINDA ESTÁ ATUALIZANDO
					//TODO: !1 DEPOIS ATUALIZAR TODOS E EXIBIR UM TOEAST
					
				}
			});
		}else{
			Toast.makeText(getContext(), "Não encontrado: "+e.getThrowable().getMessage(), Toast.LENGTH_SHORT).show();
		}
	}
	
	protected Context getContext(){
		return this.context;
	}
}
