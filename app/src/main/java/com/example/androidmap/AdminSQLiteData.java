package com.example.androidmap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AdminSQLiteData extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "puntos.db";
    private static final int DATABASE_VERSION = 10;

    public static final String TABLE_LIMA = "Lima";
    public static final String TABLE_AREQUIPA = "Arequipa";
    public static final String TABLE_CUSCO = "Cusco";
    public static final String TABLE_TRUJILLO = "Trujillo";
    public static final String TABLE_IQUITOS = "Iquitos";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "nombre";
    public static final String COLUMN_LATITUDE = "latitud";
    public static final String COLUMN_LONGITUDE = "longitud";

    // Constructor
    public AdminSQLiteData(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Crear tablas para Lima, Arequipa, Cusco, Trujillo e Iquitos
        createTable(db, TABLE_LIMA);
        createTable(db, TABLE_AREQUIPA);
        createTable(db, TABLE_CUSCO);
        createTable(db, TABLE_TRUJILLO);
        createTable(db, TABLE_IQUITOS);

        // Insertar datos iniciales
        insertInitialData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LIMA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_AREQUIPA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CUSCO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRUJILLO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_IQUITOS);
        onCreate(db);
    }

    private void createTable(SQLiteDatabase db, String tableName) {
        String CREATE_TABLE = "CREATE TABLE " + tableName + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_LATITUDE + " REAL,"
                + COLUMN_LONGITUDE + " REAL" + ")";
        db.execSQL(CREATE_TABLE);
    }

    private void insertInitialData(SQLiteDatabase db) {

        addPoint(db, TABLE_LIMA, "Plaza Mayor de Lima", -12.0463731, -77.042754);
        addPoint(db, TABLE_LIMA, "Circuito Mágico del Agua", -12.0700991, -77.0334108);
        addPoint(db, TABLE_LIMA, "Museo Larco", -12.0882764, -77.0706421);
        addPoint(db, TABLE_LIMA, "Huaca Pucllana", -12.1087365, -77.0284233);
        addPoint(db, TABLE_LIMA, "Parque Kennedy", -12.1203539, -77.0298762);

        addPoint(db, TABLE_AREQUIPA, "Plaza de Armas de Arequipa", -16.3988031, -71.5369607);
        addPoint(db, TABLE_AREQUIPA, "Monasterio de Santa Catalina", -16.3946988, -71.5367395);
        addPoint(db, TABLE_AREQUIPA, "Mirador de Yanahuara", -16.3857685, -71.5482559);
        addPoint(db, TABLE_AREQUIPA, "Museo Santuarios Andinos", -16.3989117, -71.5359385);
        addPoint(db, TABLE_AREQUIPA, "Molino de Sabandía", -16.449384, -71.511374);

        addPoint(db, TABLE_CUSCO, "Plaza de Armas de Cusco", -13.522778, -71.967223);
        addPoint(db, TABLE_CUSCO, "Sacsayhuamán", -13.512226, -71.987532);
        addPoint(db, TABLE_CUSCO, "Qorikancha", -13.518545, -71.981965);
        addPoint(db, TABLE_CUSCO, "Catedral del Cusco", -13.518611, -71.978056);
        addPoint(db, TABLE_CUSCO, "Barrio de San Blas", -13.516667, -71.978056);

        addPoint(db, TABLE_TRUJILLO, "Plaza de Armas de Trujillo", -8.109050, -79.028785);
        addPoint(db, TABLE_TRUJILLO, "Chan Chan", -8.107571, -79.028185);
        addPoint(db, TABLE_TRUJILLO, "Museo de Arqueología de la Universidad Nacional de Trujillo", -8.108456, -79.029106);
        addPoint(db, TABLE_TRUJILLO, "Huacas del Sol y de la Luna", -8.114243, -79.030073);
        addPoint(db, TABLE_TRUJILLO, "Playa Huanchaco", -8.092030, -79.040063);

        addPoint(db, TABLE_IQUITOS, "Plaza de Armas de Iquitos", -3.743467, -73.253761);
        addPoint(db, TABLE_IQUITOS, "Museo Amazónico", -3.740711, -73.253634);
        addPoint(db, TABLE_IQUITOS, "Centro de Rescate de Fauna Silvestre", -3.730320, -73.252858);
        addPoint(db, TABLE_IQUITOS, "Casa de Fierro", -3.741908, -73.251119);
        addPoint(db, TABLE_IQUITOS, "Malecón Tarapacá", -3.743865, -73.249972);
    }

    private void addPoint(SQLiteDatabase db, String tableName, String nombre, double latitud, double longitud) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, nombre);
        values.put(COLUMN_LATITUDE, latitud);
        values.put(COLUMN_LONGITUDE, longitud);
        db.insert(tableName, null, values);
    }

    public Cursor getAllPoints(String tableName) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + tableName, null);
    }
}
