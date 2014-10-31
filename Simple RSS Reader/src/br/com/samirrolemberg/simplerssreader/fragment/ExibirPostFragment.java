package br.com.samirrolemberg.simplerssreader.fragment;

import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Toast;
import br.com.samirrolemberg.simplerssreader.R;
import br.com.samirrolemberg.simplerssreader.conn.DatabaseManager;
import br.com.samirrolemberg.simplerssreader.dao.DAOConteudo;
import br.com.samirrolemberg.simplerssreader.dao.DAODescricao;
import br.com.samirrolemberg.simplerssreader.dialog.DetalhesPerguntaDialogPost;
import br.com.samirrolemberg.simplerssreader.model.Conteudo;
import br.com.samirrolemberg.simplerssreader.model.Descricao;
import br.com.samirrolemberg.simplerssreader.model.Post;
import br.com.samirrolemberg.simplerssreader.u.U;

public class ExibirPostFragment extends Fragment {
	/**
	 * The fragment argument representing the section number for this
	 * fragment.
	 */
	private static final String ARG_POST_CONTENT= "post_content";

	//private Feed feedAux = null;
	private Post postAux = null;

	private List<Conteudo> conteudosFrag = null;
	private List<Descricao> descricoesFrag = null;
	
	private DAOConteudo daoConteudo = null;
	private DAODescricao daoDescricao = null;
	
	/**
	 * Returns a new instance of this fragment for the given section number.
	 */
	public static ExibirPostFragment newInstance(int sectionNumber, Bundle args) {
		ExibirPostFragment fragment = new ExibirPostFragment();
		//Bundle args = new Bundle();
		args.putInt(ARG_POST_CONTENT, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}

	public ExibirPostFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//feedAux = (Feed) getArguments().get("Feed");
		postAux = (Post) getArguments().get("Post");
		
		daoConteudo = new DAOConteudo(getActivity());
		daoDescricao = new DAODescricao(getActivity());
		
		conteudosFrag = daoConteudo.listarTudo(postAux);
		descricoesFrag = daoDescricao.listarTudo(postAux);
		
		DatabaseManager.getInstance().closeDatabase();
		DatabaseManager.getInstance().closeDatabase();

	}
	private class JSInterface {
		private Activity activity;
		@SuppressWarnings("unused")
		private JSInterface() {
			new Exception();
		}
		public JSInterface(Activity activity){
			this.activity = activity;
		}
		@JavascriptInterface
		public void toast(String toast){
			Toast.makeText(activity, toast, Toast.LENGTH_LONG).show();
		}
		
		@SuppressLint("InflateParams")
		@JavascriptInterface
		public void abrir(String url){
			LayoutInflater inflater = activity.getLayoutInflater();
			View pergunta = (new DetalhesPerguntaDialogPost(activity,  inflater.inflate(R.layout.dialog_pergunta_feed, null), postAux,"Como deseja abrir o vídeo?")).create();
			final Uri uri = Uri.parse(url);
			final Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			new AlertDialog.Builder(activity)
			.setView(pergunta)
			.setPositiveButton("Navegador", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					open(intent);
				}
			})
			.setNegativeButton("Aplicativo", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					intent.setDataAndType(uri, "video/mp4");
					open(intent);
				}
			})
			.show();
		}
		
		private void open(final Intent intent){
			if (U.isConnected(activity)) {
				startActivity(intent);				
			}else{
				Toast.makeText(activity, "Não há conexão de internet.", Toast.LENGTH_SHORT).show();					
			}
		}
		
	}
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_exibir_post, container, false);
		WebView navegador = (WebView) rootView.findViewById(R.id.navegador__exibir_post_fragment);
		
		daoConteudo = new DAOConteudo(getActivity());
		daoDescricao = new DAODescricao(getActivity());
		
		navegador.getSettings().setBlockNetworkLoads(true);
		navegador.getSettings().setJavaScriptEnabled(true);
		navegador.setWebChromeClient(new WebChromeClient());
		navegador.addJavascriptInterface(new JSInterface(getActivity()), "Android");
		
		if (daoConteudo.size(postAux)>0) {//se o conteudo total não é nulo
			String data = conteudosFrag.get(0).getValor();
			String mimeType = "text/html";
			data = data.replace("src=\"//", "src=\"http://");

			data = returnData(data);
			
			navegador.loadData(data, mimeType+"; charset=UTF-8",null);
			Log.d("MAVEGADOR", data);
		}else if (daoDescricao.size(postAux)>0){//se a não há conteudo mas há descrição
			String data = descricoesFrag.get(0).getValor();
			data = data.replace("src=\"//", "src=\"http://");

			String mimeType = "text/html";
			
			data = returnData(data);

			navegador.loadData(data, mimeType+"; charset=UTF-8",null);
			Log.d("MAVEGADOR", data);
		}
		DatabaseManager.getInstance().closeDatabase();
		DatabaseManager.getInstance().closeDatabase();
		
		setHasOptionsMenu(true);
		return rootView;
	}
	
	private String returnData(String data){
		Document doc = Jsoup.parse(data);
		
		Element elem = doc.head();
		String script = "<script type=\"text/javascript\">"+
		"function toast(toast) {"+
		"    Android.abrir(toast);"+
		"}"+
		"</script>";
		elem.append(script);
		
		Elements elements = doc.select("iframe");
		elements.tagName("button");
		int i = 1;
		for (Element element : elements) {
			String src = element.getElementsByTag("button").attr("src");

			for (Attribute attr : element.attributes().asList()) {
				element.removeAttr(attr.getKey());
			}
			element.attr("onClick", "toast('"+src+"')");
			element.text("Assistir ao Video "+i);
			i++;
		}
		
		return data = doc.html();
	}

}
