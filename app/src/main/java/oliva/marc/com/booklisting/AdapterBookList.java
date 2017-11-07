package oliva.marc.com.booklisting;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by marc on 18/08/2017.
 */

public class AdapterBookList extends ArrayAdapter<Book> {


    public AdapterBookList(@NonNull Activity context, ArrayList<Book> booklist) {
        super(context, 0, booklist);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listView = convertView;

        if (listView == null) {
            listView = LayoutInflater.from(getContext()).inflate(R.layout.item_book, parent, false);
        }

        Book book = getItem(position);

        TextView titleTextView = listView.findViewById(R.id.title_book_text_view);
        titleTextView.setText(book.getmTitle());

        TextView autorsTextView = listView.findViewById(R.id.autors_book_text_view);
        autorsTextView.setText(book.getmAutors());

        TextView descriptionTextView = listView.findViewById(R.id.description_book_text_view);
        descriptionTextView.setText(book.getmDescription());

        ImageView portadaImageView = listView.findViewById(R.id.portada_image_view);
        portadaImageView.setImageBitmap(book.getmUrlImage());
        return listView;

    }
}
