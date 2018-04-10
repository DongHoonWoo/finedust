package dust.fine.fun.studio.finedust;

import android.os.AsyncTask;
import android.widget.TextView;

import retrofit2.Call;

public class NetworkCall extends AsyncTask<Call, Void, String> {
    TextView result;

    NetworkCall(TextView resultTextView) {
        this.result = resultTextView;
    }

    @Override
    protected void onPostExecute(String s) {
        if (result != null) {
            result.setText(s);
        }
    }

    @Override
    protected String doInBackground(Call[] calls) {
//        Call<MeasurmentSatationResponse> call = calls[0];
//        try {
//            Response<MeasurmentSatationResponse> response = call.execute();
//            return response.body().toString();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        return "";
    }
}
