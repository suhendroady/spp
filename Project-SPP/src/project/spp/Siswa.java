/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.spp;

import Config.koneksiDB;
import java.sql.Connection;
import java.text.DateFormat;
import java.util.Date;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**
 *
 * @author Sammy Guergachi <sguergachi at gmail.com>
 */
public class Siswa extends javax.swing.JFrame {

    /**
     * Creates new form Setting_SPP
     */
    public Siswa() {
        initComponents();
        load_table();
        width_column();
        tampil_kelas();
        tampil_spp();
        btnEdit.setEnabled(false);
        btnCancel.setEnabled(false);
        btnHapus.setEnabled(false);
        txtIdKelas.setVisible(true);
    }
    
    private void kosong(){
        txtNisn.setText("");
        txtNis.setText("");
        txtNamaSiswa.setText("");
        cbKelas.setSelectedItem("-Pilih Kelas-");
        txtAlamat.setText("");
        txtNoTelp.setText("");
        cbIdSPP.setSelectedItem("-Pilih Tahun SPP-");
        txtNominal.setText("Rp. ");
    }
    
    private void load_table(){        
        // membuat tampilan model tabel
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("No.");
        model.addColumn("NISN.");
        model.addColumn("Nama Siswa");
        model.addColumn("Tingkat");
        model.addColumn("Jurusan");
        model.addColumn("SPP");
        
        //menampilkan data database kedalam tabel
        try {
            int no=1;
            
            //Buat variabel untuk mengambil tanggal hari ini
            Date currentDate = new Date();
            
            String sql = "SELECT * FROM siswa sw, kelas kl, spp sp, jurusan jr WHERE "
                    + "sw.id_kelas=kl.id_kelas AND kl.id_jurusan=jr.id_jurusan AND sw.id_spp=sp.id_spp ORDER BY sw.id_kelas DESC";
            java.sql.Connection conn=(Connection)koneksiDB.connectDB();
            java.sql.Statement stm=conn.createStatement();
            java.sql.ResultSet res=stm.executeQuery(sql);
            while(res.next()){
                String nisn = res.getString("nisn");
                String nis = res.getString("nis"); 
                String nama_siswa = res.getString("nama_siswa"); 
                String nama_kelas = res.getString("nama_kelas");
                String jurusan = res.getString("jurusan");
                
                //mengambil Tahun Angkatan dari DB
                String tahun = res.getString("tahun_angkatan");
                //menjadikan tahun angkatan sesuai dengan format sekolah 1-Juli-[tahun_angkatan]
                String tahun_terhitung = "01-07-"+tahun;
                
                //Selanjutnya perlu untuk mengubah format tanggal dari String ke tipe Date, 
                //gunakan SimpleDateFormat untuk memformat tanggal yang sebelumnya ditulis dalam bentuk String
                DateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                
                //Selanjutnya lakukan parsing tanggal dari format String menjadi Date
                Date tahunMasuk = format.parse(tahun_terhitung);
                
                //konversi dari tipe data Date ke tipe data Long
                long tahunMasuk1 = tahunMasuk.getTime(); 
                long tahunSekarang1 = currentDate.getTime(); 
                
                //Setelah tanggal berhasil diparsing ke tipe Date, 
                //dapat dicari selisih waktunya dari tanggal hari ini dalam satuan miliseconds
                long diff = tahunSekarang1 - tahunMasuk1;
                //mencari selisih dengan format hitungan Bulan
                long lama = (diff / (24 * 60 * 60 * 1000))/30;
                String tingkat = Long.toString(lama);
                
                int tingkat_int;
                tingkat_int = Integer.parseInt(tingkat);
                String kelas="";
                if (tingkat_int <= 12) {
                    kelas = "X "+ nama_kelas;
                } else if (tingkat_int <= 24) {
                    kelas = "XI "+ nama_kelas;
                } else if(tingkat_int <= 36) {
                    kelas = "XII "+ nama_kelas;
                } else if(tingkat_int > 36) {
                    kelas = "Alumni "+ nama_kelas;
                }                
                
                //mengatur menjadi penyebutan dalam Rupiah
                Double y = Double.parseDouble(res.getString("nominal"));
                NumberFormat nf = NumberFormat.getCurrencyInstance();
                String nominal = nf.format(y);
                
                model.addRow(new Object[]{no++,nisn, nama_siswa, kelas, jurusan, nominal});
            }
            tblSiswa.setModel(model);
        } catch (Exception e) {
        }
    }
    
    public void width_column(){        
        TableColumn column;
        tblSiswa.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF); 
        //Nomor Urut
        column = tblSiswa.getColumnModel().getColumn(0); 
        column.setPreferredWidth(40);
        //NISN
        column = tblSiswa.getColumnModel().getColumn(1); 
        column.setPreferredWidth(140); 
        //Nama Siswa
        column = tblSiswa.getColumnModel().getColumn(2); 
        column.setPreferredWidth(280); 
        //Tingkat
        column = tblSiswa.getColumnModel().getColumn(3); 
        column.setPreferredWidth(80);        
        //jurusan
        column = tblSiswa.getColumnModel().getColumn(4); 
        column.setPreferredWidth(60);      
        //Nominal SPP
        column = tblSiswa.getColumnModel().getColumn(5); 
        column.setPreferredWidth(100);      
        //Kelas
        //column = tblKelas.getColumnModel().getColumn(6); 
        //column.setPreferredWidth(75);
    }   
   
    /*private void date(){
        //mengambil tanggal hari ini
        SimpleDateFormat dateFormat =new SimpleDateFormat("dd-MM-yyyy");
        java.util.Date date = new java.util.Date();
        txtTgl.setText(dateFormat.format(date));
    }*/
        
    public void tampil_kelas(){
        
         try {
            int no=1;
            //Buat variabel untuk mengambil tanggal hari ini
            Date currentDate = new Date();
            
            String sql = "SELECT * FROM kelas ORDER BY kode_kelas DESC";
            java.sql.Connection conn=(Connection)koneksiDB.connectDB();
            java.sql.Statement stm=conn.createStatement();
            java.sql.ResultSet res=stm.executeQuery(sql);
            
            cbKelas.addItem("-Pilih Kelas-");
            while(res.next()){
                
                String nama = res.getString("nama_kelas"); 
                
                //mengambil Tahun Angkatan dari DB
                String tahun = res.getString("tahun_angkatan");
                //menjadikan tahun angkatan sesuai dengan format sekolah 1-Juli-[tahun_angkatan]
                String tahun_terhitung = "01-07-"+tahun;
                
                //Selanjutnya perlu untuk mengubah format tanggal dari String ke tipe Date, 
                //gunakan SimpleDateFormat untuk memformat tanggal yang sebelumnya ditulis dalam bentuk String
                DateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                
                //Selanjutnya lakukan parsing tanggal dari format String menjadi Date
                Date tahunMasuk = format.parse(tahun_terhitung);
                
                //konversi dari tipe data Date ke tipe data Long
                long tahunMasuk1 = tahunMasuk.getTime(); 
                long tahunSekarang1 = currentDate.getTime(); 
                
                //Setelah tanggal berhasil diparsing ke tipe Date, 
                //dapat dicari selisih waktunya dari tanggal hari ini dalam satuan miliseconds
                long diff = tahunSekarang1 - tahunMasuk1;
                //mencari selisih dengan format hitungan Bulan
                long lama = (diff / (24 * 60 * 60 * 1000))/30;
                String tingkat = Long.toString(lama);
                
                int tingkat_int;
                tingkat_int = Integer.parseInt(tingkat);
                String kelas="";
                if (tingkat_int <= 12) {
                    kelas = "X "+ nama;
                } else if (tingkat_int <= 24) {
                    kelas = "XI "+ nama;
                } else if(tingkat_int <= 36) {
                    kelas = "XII "+ nama;
                } else if(tingkat_int > 36) {
                    kelas = "Alumni "+ nama;
                }
                cbKelas.addItem(kelas+"-"+(res.getString("id_kelas")));
            }
        }
        catch(Exception ex){
        }
    }
        
    public void tampil_id_kelas(){
        String kelas;
        kelas = (String) cbKelas.getSelectedItem();
        String[] kata = kelas.split("\\-");
        
        String idKelas = kata[1];
        
        txtIdKelas.setText(idKelas);
    }
    
    public void tampil_spp(){
        
         try {
            int no=1;            
            String sql = "SELECT * FROM spp ORDER BY tahun DESC";
            java.sql.Connection conn=(Connection)koneksiDB.connectDB();
            java.sql.Statement stm=conn.createStatement();
            java.sql.ResultSet res=stm.executeQuery(sql);
            
            cbIdSPP.addItem("-Pilih Tahun SPP-");
            while(res.next()){
                cbIdSPP.addItem(res.getString("id_spp"));
            }
        }
        catch(Exception ex){
        }
    }
    
    public void tampil_nominal(){
         try {
            int no=1;
            String sql = "SELECT nominal FROM spp WHERE id_spp= '" +cbIdSPP.getSelectedItem()+ "'";
            java.sql.Connection conn=(Connection)koneksiDB.connectDB();
            java.sql.Statement stm=conn.createStatement();
            java.sql.ResultSet res=stm.executeQuery(sql);
            
            while(res.next()){
                //mengatur menjadi penyebutan dalam Rupiah
                Double y = Double.parseDouble(res.getString("nominal"));
                NumberFormat nf = NumberFormat.getCurrencyInstance();
                String nominal = nf.format(y);
                txtNominal.setText(nominal);
            }
        }
        catch(Exception ex){
        }
    }
    
    public void simpan_siswa(){
        String nisn, nis, nama_siswa, id_kelas, alamat, no_telp, id_spp;
        nisn = txtNisn.getText();
        nis = txtNis.getText();
        nama_siswa = txtNamaSiswa.getText();
        id_kelas = txtIdKelas.getText();
        alamat = txtAlamat.getText();
        no_telp = txtNoTelp.getText();
        id_spp = (String) cbIdSPP.getSelectedItem();
       
            if (JOptionPane.showConfirmDialog(null, "Apakah Anda Yakin Ingin Menyimpan?", "Window Save", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {

                try {

                    java.sql.Connection conn = (Connection) koneksiDB.connectDB();
                    //INPUT KE Table PEMINJAMAN
                    String sql = "INSERT INTO siswa (nisn, nis, nama_siswa, id_kelas, alamat, no_tlp, id_spp)"
                                                      + "VALUES ('" + nisn + "','" + nis + "','" + nama_siswa + "',"
                                                      + "'" + id_kelas + "','" + alamat + "','" + no_telp + "','" + id_spp + "')";                                
                    java.sql.PreparedStatement pst = conn.prepareStatement(sql);
                    pst.execute();

                    JOptionPane.showMessageDialog(null, "Penyimpanan Data Berhasil");
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, e.getMessage());
                }            
            load_table();            
            width_column();
            kosong();          
        }
    }
    
    public void edit_siswa(){
        String nisn, nis, nama_siswa, id_kelas, alamat, no_telp, id_spp;
        id_kelas = txtIdKelas.getText();
        nisn = txtNisn.getText();
        nis = txtNis.getText();
        nama_siswa = txtNamaSiswa.getText();
        alamat = txtAlamat.getText();
        no_telp = txtNoTelp.getText();
        id_spp = (String) cbIdSPP.getSelectedItem();
                
        //Konversi String ke Nilai Integer supaya dapat dibandingkan
        /*int jumlah_pinjam_int, stok_int, sisa_stok;
        jumlah_pinjam_int = Integer.parseInt(jumlah); //variabel Jumlah Barang dipinjam
        stok_int = Integer.parseInt(stok); //variabel jumlah stok barang di Database
        sisa_stok = stok_int - jumlah_pinjam_int;
                
        String tgl_pinjam = "yyyy-MM-dd";
        SimpleDateFormat fm = new SimpleDateFormat (tgl_pinjam);
        String tanggal_pinjam = String.valueOf(fm.format(tglPinjam.getDate()));
        */
       
            if (JOptionPane.showConfirmDialog(null, "Apakah Anda Yakin Ingin Menyimpan?", "Window Save", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {

                try {

                    java.sql.Connection conn = (Connection) koneksiDB.connectDB();
                    //INPUT KE Table Siswa
                    String sql = "UPDATE siswa SET nis = '" + nis + "', nama_siswa = '" + nama_siswa + "', id_kelas = '" + id_kelas + "',"
                            + "alamat = '" + alamat + "', no_tlp = '" + no_telp + "', id_spp = '" + id_spp + "' WHERE nisn = '" + nisn + "'";
                    java.sql.PreparedStatement pstb = conn.prepareStatement(sql);
                    pstb.execute();

                    JOptionPane.showMessageDialog(null, "Penyimpanan Data Berhasil");
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, e.getMessage());
                }            
            load_table();            
            width_column();
            kosong();
            btnSimpan.setEnabled(true);
            btnEdit.setEnabled(false);
            btnCancel.setEnabled(false);
            btnHapus.setEnabled(false);
        }
    }
    
    private void hapus_siswa(){
        String nisn;
        nisn = txtNisn.getText();
       
            if (JOptionPane.showConfirmDialog(null, "Apakah Anda Yakin Ingin Menghapus?", "Window Save", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {

                try {

                    java.sql.Connection conn = (Connection) koneksiDB.connectDB();
                    //Delete data sesuai NISN
                    String sql = "DELETE FROM siswa WHERE nisn = '" + nisn + "'";
                    java.sql.PreparedStatement pstb = conn.prepareStatement(sql);
                    pstb.execute();

                    JOptionPane.showMessageDialog(null, "Data Berhasil Dihapus");
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, e.getMessage());
                }            
            load_table();            
            width_column();
            kosong();
            btnHapus.setEnabled(false);
            btnSimpan.setEnabled(true);
            btnEdit.setEnabled(false);
            btnCancel.setEnabled(false);
            txtNisn.setEnabled(true);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtIdKelas = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtNisn = new javax.swing.JTextField();
        txtNis = new javax.swing.JTextField();
        cbKelas = new javax.swing.JComboBox<>();
        btnSimpan = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblSiswa = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        btnEdit = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtNamaSiswa = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtAlamat = new javax.swing.JTextArea();
        txtNoTelp = new javax.swing.JTextField();
        cbIdSPP = new javax.swing.JComboBox<>();
        jLabel10 = new javax.swing.JLabel();
        txtNominal = new javax.swing.JLabel();
        btnHapus = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 51));

        jLabel1.setFont(new java.awt.Font("Lato", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 51, 102));
        jLabel1.setText("Data Siswa");

        txtIdKelas.setForeground(new java.awt.Color(0, 0, 0));
        txtIdKelas.setText("idKelas");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel1)
                .addGap(325, 325, 325)
                .addComponent(txtIdKelas)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtIdKelas)
                    .addComponent(jLabel1))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel2.setFont(new java.awt.Font("Lato", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 0));
        jLabel2.setText("N I S N               :");

        jLabel3.setFont(new java.awt.Font("Lato", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jLabel3.setText("N I S                   :");

        jLabel4.setFont(new java.awt.Font("Lato", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 0, 0));
        jLabel4.setText("Nama Siswa    :");

        cbKelas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbKelasActionPerformed(evt);
            }
        });

        btnSimpan.setBackground(new java.awt.Color(51, 255, 51));
        btnSimpan.setFont(new java.awt.Font("Lato", 1, 14)); // NOI18N
        btnSimpan.setForeground(new java.awt.Color(255, 255, 255));
        btnSimpan.setText("Simpan");
        btnSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSimpanActionPerformed(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(204, 255, 153));
        jPanel2.setForeground(new java.awt.Color(204, 255, 153));

        tblSiswa.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tblSiswa.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblSiswa.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblSiswaMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblSiswa);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 771, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 385, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel5.setForeground(new java.awt.Color(255, 102, 51));
        jLabel5.setText("Ket: NISN tidak boleh kosong!");

        btnEdit.setBackground(new java.awt.Color(51, 153, 255));
        btnEdit.setFont(new java.awt.Font("Lato", 1, 14)); // NOI18N
        btnEdit.setForeground(new java.awt.Color(255, 255, 255));
        btnEdit.setText("Edit");
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });

        btnCancel.setBackground(new java.awt.Color(255, 51, 51));
        btnCancel.setFont(new java.awt.Font("Lato", 1, 14)); // NOI18N
        btnCancel.setForeground(new java.awt.Color(255, 255, 255));
        btnCancel.setText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Lato", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 0, 0));
        jLabel6.setText("Kelas                 :");

        jLabel7.setFont(new java.awt.Font("Lato", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 0, 0));
        jLabel7.setText("Alamat             :");

        jLabel8.setFont(new java.awt.Font("Lato", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(0, 0, 0));
        jLabel8.setText("No. Telp          :");

        jLabel9.setFont(new java.awt.Font("Lato", 1, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(0, 0, 0));
        jLabel9.setText("ID. SPP             :");

        txtAlamat.setColumns(20);
        txtAlamat.setRows(5);
        jScrollPane2.setViewportView(txtAlamat);

        cbIdSPP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbIdSPPActionPerformed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Lato", 1, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(0, 0, 0));
        jLabel10.setText("Nominal           :");

        txtNominal.setFont(new java.awt.Font("Lato", 1, 14)); // NOI18N
        txtNominal.setForeground(new java.awt.Color(0, 0, 0));
        txtNominal.setText("Rp.");

        btnHapus.setBackground(new java.awt.Color(255, 153, 51));
        btnHapus.setForeground(new java.awt.Color(255, 255, 255));
        btnHapus.setText("Hapus");
        btnHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel3))
                                .addGroup(layout.createSequentialGroup()
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel6)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel8)
                                            .addComponent(jLabel7))
                                        .addGroup(layout.createSequentialGroup()
                                            .addComponent(jLabel9)
                                            .addGap(1, 1, 1)))
                                    .addGap(5, 5, 5)))
                            .addComponent(jLabel10)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(2, 2, 2)
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(30, 30, 30)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cbIdSPP, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE)
                                .addComponent(cbKelas, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtNisn)
                                .addComponent(txtNis)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                    .addComponent(txtNoTelp)
                                    .addGap(3, 3, 3))
                                .addComponent(txtNamaSiswa))
                            .addComponent(txtNominal)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnHapus, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnSimpan)
                        .addGap(16, 16, 16)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtNisn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(3, 3, 3)
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtNis, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtNamaSiswa, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cbKelas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addGap(95, 95, 95))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtNoTelp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel8))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(cbIdSPP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel9))))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(txtNominal))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnEdit)
                            .addComponent(btnSimpan)
                            .addComponent(btnCancel)
                            .addComponent(btnHapus)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 55, Short.MAX_VALUE)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanActionPerformed
        // TODO add your handling code here:
        simpan_siswa();
    }//GEN-LAST:event_btnSimpanActionPerformed

    private void cbKelasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbKelasActionPerformed
        // TODO add your handling code here:
        tampil_id_kelas();
    }//GEN-LAST:event_cbKelasActionPerformed

    private void tblSiswaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSiswaMouseClicked
        // TODO add your handling code here:
        int baris = tblSiswa.rowAtPoint(evt.getPoint());
        String nisn = tblSiswa.getValueAt(baris, 1).toString();
        
        try {
            //Buat variabel untuk mengambil tanggal hari ini
            Date currentDate = new Date();
            //int no=1;
            String sql = "SELECT * FROM siswa sw, spp sp, kelas kl, jurusan jr WHERE "
                        + "sw.id_kelas=kl.id_kelas AND sw.id_spp=sp.id_spp AND sp.id_jurusan = jr.id_jurusan AND sw.nisn = '" + nisn + "'";
            java.sql.Connection conn=(Connection)koneksiDB.connectDB();
            java.sql.Statement stm=conn.createStatement();
            java.sql.ResultSet res=stm.executeQuery(sql);
            while(res.next()){
                String nis = res.getString("nis");
                String nama_siswa = res.getString("nama_siswa");                
                String nama_kelas = res.getString("nama_kelas");
                String tahun = res.getString("tahun_angkatan");
                String alamat = res.getString("alamat");
                String no_telp = res.getString("no_tlp");
                String id_spp = res.getString("id_spp");
                String nominal = res.getString("nominal");
                
                //menjadikan tahun angkatan sesuai dengan format sekolah 1-Juli-[tahun_angkatan]
                String tahun_terhitung = "01-07-"+tahun;
                
                //Selanjutnya perlu untuk mengubah format tanggal dari String ke tipe Date, 
                //gunakan SimpleDateFormat untuk memformat tanggal yang sebelumnya ditulis dalam bentuk String
                DateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                
                //Selanjutnya lakukan parsing tanggal dari format String menjadi Date
                Date tahunMasuk = format.parse(tahun_terhitung);
                
                //konversi dari tipe data Date ke tipe data Long
                long tahunMasuk1 = tahunMasuk.getTime(); 
                long tahunSekarang1 = currentDate.getTime(); 
                
                //Setelah tanggal berhasil diparsing ke tipe Date, 
                //dapat dicari selisih waktunya dari tanggal hari ini dalam satuan miliseconds
                long diff = tahunSekarang1 - tahunMasuk1;
                //mencari selisih dengan format hitungan Bulan
                long lama = (diff / (24 * 60 * 60 * 1000))/30;
                String tingkat = Long.toString(lama);
                
                int tingkat_int;
                tingkat_int = Integer.parseInt(tingkat);
                String kelas="";
                if (tingkat_int <= 12) {
                    kelas = "X "+ nama_kelas;
                } else if (tingkat_int <= 24) {
                    kelas = "XI "+ nama_kelas;
                } else if(tingkat_int <= 36) {
                    kelas = "XII "+ nama_kelas;
                } else if(tingkat_int > 36) {
                    kelas = "Alumni "+ nama_kelas;
                }
                cbKelas.setSelectedItem(kelas+"-"+(res.getString("id_kelas")));
                
                txtNisn.setText(nisn);
                txtNis.setText(nis);
                txtNamaSiswa.setText(nama_siswa);
                txtAlamat.setText(alamat);
                txtNoTelp.setText(no_telp);
                cbIdSPP.setSelectedItem(id_spp);
                txtNominal.setText("Rp."+nominal);
                btnSimpan.setEnabled(false);
                btnEdit.setEnabled(true);
                btnCancel.setEnabled(true);
                btnHapus.setEnabled(true);
                txtNisn.setEnabled(false);
                //cbSumberDana.setSelectedItem(sumber);
            }
        }catch (Exception e) {
        }
    }//GEN-LAST:event_tblSiswaMouseClicked

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // TODO add your handling code here:
        edit_siswa();
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        btnSimpan.setEnabled(true);
        btnEdit.setEnabled(false);
        btnCancel.setEnabled(false);
        txtNisn.setEnabled(true);
        kosong();
    }//GEN-LAST:event_btnCancelActionPerformed

    private void cbIdSPPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbIdSPPActionPerformed
        // TODO add your handling code here:
        tampil_nominal();
    }//GEN-LAST:event_cbIdSPPActionPerformed

    private void btnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusActionPerformed
        // TODO add your handling code here:
        hapus_siswa();
    }//GEN-LAST:event_btnHapusActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Siswa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Siswa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Siswa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Siswa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Siswa().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnSimpan;
    private javax.swing.JComboBox<String> cbIdSPP;
    private javax.swing.JComboBox<String> cbKelas;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable tblSiswa;
    private javax.swing.JTextArea txtAlamat;
    private javax.swing.JLabel txtIdKelas;
    private javax.swing.JTextField txtNamaSiswa;
    private javax.swing.JTextField txtNis;
    private javax.swing.JTextField txtNisn;
    private javax.swing.JTextField txtNoTelp;
    private javax.swing.JLabel txtNominal;
    // End of variables declaration//GEN-END:variables
}
