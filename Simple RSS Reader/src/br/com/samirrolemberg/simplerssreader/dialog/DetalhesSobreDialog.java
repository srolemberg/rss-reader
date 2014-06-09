package br.com.samirrolemberg.simplerssreader.dialog;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import br.com.samirrolemberg.simplerssreader.R;


public class DetalhesSobreDialog {

	private Context context;
	private View view;
	
	public DetalhesSobreDialog(Context context, View view) {
		super();
		this.context = context;
		this.view = view;
	}
	
	public View create(){
		Button contato = (Button) view.findViewById(R.id.dialog_sobre__contato);
		Button avalie = (Button) view.findViewById(R.id.dialog_sobre__avalie);
		
		contato.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_SEND);
				intent.setType("text/plain");
				intent.putExtra(Intent.EXTRA_EMAIL, "srolemberg@outlook.com");
				intent.putExtra(Intent.EXTRA_SUBJECT, "Simple RSS Reader - Contato");

				context.startActivity(Intent.createChooser(intent, "Contato"));
			}
		});
		avalie.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.pudim.com.br"));
				context.startActivity(intent);
			}
		});
		
		return view;
	}
}
