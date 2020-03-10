/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.spp;

import Config.koneksiDB;
import java.sql.Connection;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**
 *
 * @author Sammy Guergachi <sguergachi at gmail.com>
 */
public class Setting_SPP extends javax.swing.JFrame {

    /**
     * Creates new form Setting_SPP
     */
    public Setting_SPP() {
        initComponents();
        load_table();
        width_column();
        tampil_jurusan();
    }
    
    private void load_table(){
        // membuat tampilan model tabel
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("No.");
        model.addColumn("ID SPP");
        model.addColumn("Thn. Masuk");
        model.addColumn("Nominal");
        
        //menampilkan data database kedalam tabel
        try {
            int no=1;
            String sql = "SELECT * FROM spp ORDER BY id_spp DESC";
            java.sql.Connection conn=(Connection)koneksiDB.connectDB();
            java.sql.Statement stm=conn.createStatement();
            java.sql.ResultSet res=stm.executeQuery(sql);
            while(res.next()){
                String id = res.getString("id_spp");
                String tahun = res.getString("tahun"); 
                
                //mengatur menjadi penyebutan dalam Rupiah
                Double y = Double.parseDouble(res.getString("nominal"));
                NumberFormat nf = NumberFormat.getCurrencyInstance();
                String nominal = nf.format(y);
                
                model.addRow(new Object[]{no++,id, tahun, nominal});
            }
            tblSPP.setModel(model);
        } catch (Exception e) {
        }
    }
    
    public void width_column(){        
        TableColumn column;
        tblSPP.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF); 
        //nomor urut
        //column = tableInventaris.getColumnModel().getColumn(0); 
        //column.setPreferredWidth(40);
        //Nomor Urut
        column = tblSPP.getColumnModel().getColumn(0); 
        column.setPreferredWidth(40);
        //id SPP
        column = tblSPP.getColumnModel().getColumn(1); 
        column.setPreferredWidth(75); 
        //Tahun
        column = tblSPP.getColumnModel().getColumn(2); 
        column.setPreferredWidth(50); 
        //Nominal
        column = tblSPP.getColumnModel().getColumn(3); 
        column.setPreferredWidth(140);
    }
    
    public void tampil_jurusan(){
         try {
            int no=1;
            String sql = "SELECT id_jurusan, jurusan FROM jurusan ORDER BY id_jurusan ASC";
            java.sql.Connection conn=(Connection)koneksiDB.connectDB();
            java.sql.Statement stm=conn.createStatement();
            java.sql.ResultSet res=stm.executeQuery(sql);
            
            cbJurusan.addItem("-Pilih Jurusan-");
            while(res.next()){
                cbJurusan.addItem(res.getString("jurusan"));
            }
        }
        catch(Exception ex){
        }
    }
    
    public void tampil_id_jurusan(){
         try {
            int no=1;
            String sql = "SELECT id_jurusan, jurusan FROM jurusan WHERE jurusan= '" +cbJurusan.getSelectedItem()+ "'";
            java.sql.Connection conn=(Connection)koneksiDB.connectDB();
            java.sql.Statement stm=conn.createStatement();
            java.sql.ResultSet res=stm.executeQuery(sql);
            
            while(res.next()){
                txtIdJurusan.setText(res.getString("id_jurusan"));
            }
        }
        catch(Exception ex){
        }
    }
    
    public void simpan_setting_spp(){
        String tahun_masuk, nominal, id_jurusan, id_spp, jurusan;
        tahun_masuk=txtTahunMasuk.getText();
        nominal=txtNominal.getText();
        id_jurusan=txtIdJurusan.getText();
        jurusan = (String) cbJurusan.getSelectedItem();
        id_spp = tahun_masuk+jurusan;
                
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
                    //INPUT KE Table PEMINJAMAN
                    String sql = "INSERT INTO spp (id_spp, tahun, nominal, id_jurusan)"
                                                      + "VALUES ('" + id_spp + "','" + tahun_masuk + "','" + nominal + "',"
                                                      + "'" + id_jurusan + "')";                                
                    java.sql.PreparedStatement pst = conn.prepareStatement(sql);
                    pst.execute();

                    JOptionPane.showMessageDialog(null, "Penyimpanan Data Berhasil");
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, e.getMessage());
                }

            load_table();
            //kosong();          
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
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtTahunMasuk = new javax.swing.JTextField();
        txtNominal = new javax.swing.JTextField();
        cbJurusan = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblSPP = new javax.swing.JTable();
        txtIdJurusan = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 51));

        jLabel1.setFont(new java.awt.Font("Lato", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 51, 102));
        jLabel1.setText("Setting SPP");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel2.setFont(new java.awt.Font("Lato", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 0));
        jLabel2.setText("Tahun Masuk Siswa   :");

        jLabel3.setFont(new java.awt.Font("Lato", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jLabel3.setText("Nominal SPP                :");

        jLabel4.setFont(new java.awt.Font("Lato", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 0, 0));
        jLabel4.setText("Jurusan                           :");

        cbJurusan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbJurusanActionPerformed(evt);
            }
        });

        jButton1.setBackground(new java.awt.Color(51, 255, 51));
        jButton1.setFont(new java.awt.Font("Lato", 1, 14)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Simpan");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(204, 255, 153));
        jPanel2.setForeground(new java.awt.Color(204, 255, 153));

        tblSPP.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(tblSPP);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 333, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE)
                .addContainerGap())
        );

        txtIdJurusan.setForeground(new java.awt.Color(51, 51, 51));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addGap(18, 18, 18)
                                .addComponent(cbJurusan, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addGap(38, 38, 38)
                                .addComponent(txtNominal, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtTahunMasuk))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(39, 39, 39)
                        .addComponent(txtIdJurusan)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(txtTahunMasuk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(txtNominal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(cbJurusan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton1)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(txtIdJurusan, javax.swing.GroupLayout.PREFERRED_SIZE, 0, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(33, 33, 33))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 7, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        simpan_setting_spp();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void cbJurusanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbJurusanActionPerformed
        // TODO add your handling code here:
        tampil_id_jurusan();
    }//GEN-LAST:event_cbJurusanActionPerformed

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
            java.util.logging.Logger.getLogger(Setting_SPP.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Setting_SPP.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Setting_SPP.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Setting_SPP.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Setting_SPP().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> cbJurusan;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblSPP;
    private javax.swing.JLabel txtIdJurusan;
    private javax.swing.JTextField txtNominal;
    private javax.swing.JTextField txtTahunMasuk;
    // End of variables declaration//GEN-END:variables
}
