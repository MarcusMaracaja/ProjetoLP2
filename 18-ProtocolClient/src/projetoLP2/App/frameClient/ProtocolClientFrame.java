/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package projetoLP2.App.frameClient;

import java.awt.Color;
import projetoLP2.App.serviceProtocolService.ConectaCliente;
import projetoLP2.AppChatProtocol.Protocol;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Set;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import projetoLP2.AppChatProtocol.Protocol.Status;


/**
 *
 * @author Marcio Ballem
 */
public class ProtocolClientFrame extends javax.swing.JFrame {

    private Socket s;
    private Protocol protocol;
    private ConectaCliente conectaCliente;

    /**
     * Creates new form ClienteFrame
     */
    public ProtocolClientFrame() { //construtor
        initComponents();
        jPanelChat.setVisible(false);
        jPanelAlugarLocal.setVisible(false);
        jPanelChat.setVisible(false);
        jPanelLocatario.setVisible(false);

    }

    private class ListenerSocket implements Runnable { //inicia a Thread

        private ObjectInputStream input; //como é o cliente, não precisa do output

        public ListenerSocket(Socket s) {
            try {
                this.input = new ObjectInputStream(s.getInputStream());
            } catch (Exception e) {
            }
        }

        @Override
        public void run() {
            Protocol protocol = null; //igual ao do servidor
            try {
                while ((protocol = (Protocol) input.readObject()) != null) {//mesmos procedimentos do servidor
                    Status status = protocol.getStatus(); //Irá reescrever a ação que o servidor está enviando
                    switch (status) {
                        case CONECTADO:
                            conectar(protocol);
                            break;
                        case DESCONECTADO:
                            desconectar(); //Chama o método desconectar
                            s.close(); // fecha o socket 
                            //(BUG encontrado quando era colocado no método,
                            //pois o while continuava rodando ma so socket, tentanso ler o input, só que 
                            //estava fechado, e gerava um erro por isso precisou ser colocado aqui)
                            break;
                        case MSG_PRIVADA:
                            System.out.println("Você diz: " + protocol.getTexto());
                            msg_recebida(protocol);
                            break;
                        case CLIENTES_ON:
                            clienteOnlines(protocol);
                            break;
                        default:
                            break;
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(ProtocolClientFrame.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ProtocolClientFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private void conectar(Protocol protocol) {//método para preencher o protocol na lista enumerada do CONECTAR
        if (protocol.getTexto().equals("Offline")) { //Não houve conexão, usado quando é negado a conexão, Ex. quando já tem o memso nome na lista dos onlines.
            this.txtName.setText(""); //apaga o nome no cliente
            JOptionPane.showMessageDialog(this, "A conexão Falhou!");//POP UP!
            return;
        }
        //O else aqui é implícito, caso não seja "offline", então ele irá conectar,então faça o que está abaixo
        this.protocol = protocol; //Pega o objeto protocol e passa o valor que recebemos como msg
        this.btnConnectarCliente.setEnabled(false); //desabilita este botão
        this.txtName.setEditable(false); //não deixa mais mudar o nome na JTextField

        this.btnSair.setEnabled(true); //habilita este botão
        this.txtAreaSend.setEnabled(true);
        this.txtAreaReceive.setEnabled(true); //habilita a área de texto que recebe
        this.btnEnviar.setEnabled(true); //habilita o botão enviar
        this.btnLimpar.setEnabled(true); //habilita o botão limpar

        JOptionPane.showMessageDialog(this, "Conexão estabelecida."); //POP UP
    } 

    private void desconectar() { //apenas habilita ou desabilita itens na tela
        //☼ ↓AREAS DE TEXTO
        this.txtName.setEditable(true);
        this.txtAreaSend.setEnabled(false);
        this.txtAreaReceive.setEnabled(false);
        //◙ BOTÕES
        this.btnConnectarCliente.setEnabled(true);
        this.btnSair.setEnabled(false);
        this.btnEnviar.setEnabled(false);
        this.btnLimpar.setEnabled(false);
        //♫ LIMPA AS CAIXAS DE TEXTO
        this.txtAreaReceive.setText("");
        this.txtAreaSend.setText("");

        System.out.println(":::Desconectado:::");

        JOptionPane.showMessageDialog(this, "CONEXÃO PERDIDA!"); //POP UP!
    }

    private void msg_recebida(Protocol protocol) {
        this.txtAreaReceive.append(protocol.getNome() + " disse: " + protocol.getTexto() + "\n");
        //o apend permite acumular várias msg que chegam e não precisa substituir uma msg por outra
    }

    public void clienteOnlines(Protocol protocol) {
        System.out.println(protocol.getSetOn().toString());//teste no console 

        Set<String> NamesC = protocol.getSetOn(); //uma set de string para recuperar quem ta on

        Vector<String> clientes = new Vector<>();
        int i = 0;
        for (String filtrado : NamesC){
            if (!filtrado.contains("***")){
                clientes.add(filtrado);
                i++;
            }
        }
        
        Vector<String> locais = new Vector<>();
        i = 0;
        for (String filtrado : NamesC){
            if (filtrado.contains("***")){
                locais.add(filtrado);
                i++;
            }
        }

        //↓ propriedades usadas no jList para seu funcionamento
        this.listOnlines.setListData(clientes); //passar o array para o jList
        this.listOnlines.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); //serve para aceitar ser um nome selecionado, sem isso era possivel selecionar vários
        this.listOnlines.setLayoutOrientation(JList.VERTICAL); //organiza os nomes verticalmente
        this.listOnlines1.setListData(locais); //passar o array para o jList
        this.listOnlines1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); //serve para aceitar ser um nome selecionado, sem isso era possivel selecionar vários
        this.listOnlines1.setLayoutOrientation(JList.VERTICAL);
    }   

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanelAlugarLocal = new javax.swing.JPanel();
        jLabelNomeProfissional = new javax.swing.JLabel();
        jTextSuaPro = new javax.swing.JTextField();
        jButtonAluAndChat = new javax.swing.JButton();
        jPanelInicial = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtName = new javax.swing.JTextField();
        btnConnectarCliente = new javax.swing.JButton();
        btnConnectarProf = new javax.swing.JButton();
        btnConnectarLoc = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jPanelChat = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        listOnlines1 = new javax.swing.JList();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        listOnlines = new javax.swing.JList();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtAreaReceive = new javax.swing.JTextArea();
        jLabelSeuNome = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtAreaSend = new javax.swing.JTextArea();
        btnEnviar = new javax.swing.JButton();
        btnLimpar = new javax.swing.JButton();
        btnSair = new javax.swing.JButton();
        jPanelLocatario = new javax.swing.JPanel();
        jLabelNomeLocatario = new javax.swing.JLabel();
        txtLocal = new javax.swing.JTextField();
        bt1 = new javax.swing.JRadioButton();
        jLabel4 = new javax.swing.JLabel();
        bt2 = new javax.swing.JRadioButton();
        bt3 = new javax.swing.JRadioButton();
        bt4 = new javax.swing.JRadioButton();
        btnCadastrarEspaco = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Tela Inicial");
        setBounds(new java.awt.Rectangle(0, 0, 0, 0));
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setMaximizedBounds(new java.awt.Rectangle(0, 0, 0, 0));
        setPreferredSize(new java.awt.Dimension(800, 600));

        jPanelAlugarLocal.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanelAlugarLocal.setToolTipText("Alugar um Espaço");

        jLabelNomeProfissional.setFont(new java.awt.Font("Times New Roman", 1, 11)); // NOI18N
        jLabelNomeProfissional.setText("Informe sua profissão");

        jButtonAluAndChat.setText("Alugar e falar com clientes");
        jButtonAluAndChat.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButtonAluAndChat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAluAndChatActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelAlugarLocalLayout = new javax.swing.GroupLayout(jPanelAlugarLocal);
        jPanelAlugarLocal.setLayout(jPanelAlugarLocalLayout);
        jPanelAlugarLocalLayout.setHorizontalGroup(
            jPanelAlugarLocalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelAlugarLocalLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelAlugarLocalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonAluAndChat)
                    .addComponent(jLabelNomeProfissional)
                    .addComponent(jTextSuaPro, javax.swing.GroupLayout.PREFERRED_SIZE, 321, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanelAlugarLocalLayout.setVerticalGroup(
            jPanelAlugarLocalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelAlugarLocalLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelNomeProfissional)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextSuaPro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButtonAluAndChat)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanelInicial.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanelInicial.setPreferredSize(new java.awt.Dimension(300, 100));
        jPanelInicial.setVerifyInputWhenFocusTarget(false);

        jPanel1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 180, Short.MAX_VALUE)
        );

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 11)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel1.setText("Digite seu nome");
        jLabel1.setToolTipText("");
        jLabel1.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        txtName.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNameActionPerformed(evt);
            }
        });

        btnConnectarCliente.setText("Cliente");
        btnConnectarCliente.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnConnectarCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConnectarClienteActionPerformed(evt);
            }
        });

        btnConnectarProf.setText("Profissional");
        btnConnectarProf.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnConnectarProf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConnectarProfActionPerformed(evt);
            }
        });

        btnConnectarLoc.setText("Locatário");
        btnConnectarLoc.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnConnectarLoc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConnectarLocActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 11)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel2.setText("Selecione seu perfil profissional");
        jLabel2.setToolTipText("");
        jLabel2.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        javax.swing.GroupLayout jPanelInicialLayout = new javax.swing.GroupLayout(jPanelInicial);
        jPanelInicial.setLayout(jPanelInicialLayout);
        jPanelInicialLayout.setHorizontalGroup(
            jPanelInicialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelInicialLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelInicialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnConnectarCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnConnectarProf, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnConnectarLoc, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanelInicialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(txtName, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 279, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel2)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );
        jPanelInicialLayout.setVerticalGroup(
            jPanelInicialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelInicialLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelInicialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanelInicialLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnConnectarProf)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnConnectarCliente)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnConnectarLoc)))
                .addGap(0, 0, 0))
        );

        jPanel1.getAccessibleContext().setAccessibleName("");

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        listOnlines1.setBackground(new java.awt.Color(204, 255, 204));
        listOnlines1.setFont(new java.awt.Font("Arial Black", 0, 12)); // NOI18N
        jScrollPane5.setViewportView(listOnlines1);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane5)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Usuários Online"));
        jPanel4.setToolTipText("");

        listOnlines.setBackground(new java.awt.Color(204, 255, 204));
        listOnlines.setFont(new java.awt.Font("Arial Black", 0, 12)); // NOI18N
        jScrollPane4.setViewportView(listOnlines);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 309, Short.MAX_VALUE)
                .addContainerGap())
        );

        txtAreaReceive.setEditable(false);
        txtAreaReceive.setColumns(20);
        txtAreaReceive.setRows(5);
        txtAreaReceive.setEnabled(false);
        jScrollPane1.setViewportView(txtAreaReceive);

        jLabelSeuNome.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        jLabelSeuNome.setText("Seu Nome");

        txtAreaSend.setColumns(20);
        txtAreaSend.setRows(5);
        txtAreaSend.setEnabled(false);
        jScrollPane2.setViewportView(txtAreaSend);

        btnEnviar.setText("Enviar");
        btnEnviar.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnEnviar.setEnabled(false);
        btnEnviar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEnviarActionPerformed(evt);
            }
        });

        btnLimpar.setText("Limpar");
        btnLimpar.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnLimpar.setEnabled(false);
        btnLimpar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimparActionPerformed(evt);
            }
        });

        btnSair.setText("Sair");
        btnSair.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnSair.setEnabled(false);
        btnSair.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSairActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelChatLayout = new javax.swing.GroupLayout(jPanelChat);
        jPanelChat.setLayout(jPanelChatLayout);
        jPanelChatLayout.setHorizontalGroup(
            jPanelChatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelChatLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelChatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanelChatLayout.createSequentialGroup()
                        .addGroup(jPanelChatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnSair, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanelChatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanelChatLayout.createSequentialGroup()
                                .addComponent(jScrollPane2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanelChatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(btnEnviar, javax.swing.GroupLayout.DEFAULT_SIZE, 72, Short.MAX_VALUE)
                                    .addComponent(btnLimpar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addComponent(jScrollPane1)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelChatLayout.createSequentialGroup()
                                .addGap(431, 431, 431)
                                .addComponent(jLabelSeuNome)))))
                .addContainerGap())
        );
        jPanelChatLayout.setVerticalGroup(
            jPanelChatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelChatLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelChatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelChatLayout.createSequentialGroup()
                        .addComponent(jLabelSeuNome)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelChatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanelChatLayout.createSequentialGroup()
                                .addComponent(btnEnviar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnLimpar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanelChatLayout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSair, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        jLabelNomeLocatario.setText("NOME");

        bt1.setText("08-10");

        jLabel4.setFont(new java.awt.Font("Times New Roman", 1, 11)); // NOI18N
        jLabel4.setText("Horários Disponíveis");

        bt2.setText("10-12");

        bt3.setText("14-16");

        bt4.setText("16-18");

        btnCadastrarEspaco.setText("Cadastrar");
        btnCadastrarEspaco.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnCadastrarEspaco.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCadastrarEspacoActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Times New Roman", 1, 11)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel3.setText("Informe o nome do local");
        jLabel3.setToolTipText("");
        jLabel3.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        javax.swing.GroupLayout jPanelLocatarioLayout = new javax.swing.GroupLayout(jPanelLocatario);
        jPanelLocatario.setLayout(jPanelLocatarioLayout);
        jPanelLocatarioLayout.setHorizontalGroup(
            jPanelLocatarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelLocatarioLayout.createSequentialGroup()
                .addGroup(jPanelLocatarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelLocatarioLayout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addGroup(jPanelLocatarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelNomeLocatario)
                            .addComponent(bt3)
                            .addComponent(bt4)
                            .addComponent(jLabel4)
                            .addGroup(jPanelLocatarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(bt1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(bt2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanelLocatarioLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanelLocatarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanelLocatarioLayout.createSequentialGroup()
                                .addGroup(jPanelLocatarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtLocal, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel3))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(btnCadastrarEspaco, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanelLocatarioLayout.setVerticalGroup(
            jPanelLocatarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelLocatarioLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelNomeLocatario)
                .addGap(4, 4, 4)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtLocal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bt1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bt2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bt3, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bt4, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnCadastrarEspaco, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 636, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanelAlugarLocal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanelInicial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanelChat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanelLocatario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 546, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanelAlugarLocal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanelInicial, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanelChat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanelLocatario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSairActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSairActionPerformed
        //o botão sair desconecta sem que o programa feche, permitindo reconectar
        Protocol protocol = new Protocol();//instancia essa ação
        protocol.setNome(this.protocol.getNome()); //coloca o nome desse usuario no protocol
        protocol.setStatus(Status.DESCONECTADO);//Setamos seu status
        this.conectaCliente.enviar(protocol);//envia esse status para o servidor
        desconectar();
    }//GEN-LAST:event_btnSairActionPerformed

    private void btnLimparActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimparActionPerformed
        this.txtAreaSend.setText(""); //teste de limpeza de área de texto: OK
    }//GEN-LAST:event_btnLimparActionPerformed

    private void btnEnviarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEnviarActionPerformed

        String text = this.txtAreaSend.getText(); //Cria variavesrecuperadas desta area
        String name = this.protocol.getNome(); //captura o nome

        this.protocol = new Protocol(); // cria a instancia

        if (this.listOnlines.getSelectedIndex() > -1) { //Teste para testar um nome selecionado
            //no jList há um método getSelectedIndex que fica com -1 se n estiver ninguém selecionado
            //por isso esse if diz: Se for maior que -1, então está selecionado
            this.protocol.setNomeNalista((String) this.listOnlines.getSelectedValue()); //cast pra string para poder setar
            this.protocol.setStatus(Status.MSG_PRIVADA); //envia a msg para o selecionado
            this.listOnlines.clearSelection(); // limpa a seleção para não ficar eternamente selecionado
        } else {
            this.protocol.setStatus(Status.MSG_ENV);// Sua ação é feita para enviar msg a todos
            //JOptionPane.showMessageDialog(this, "SELECIONE UM PROFISSIONAL!"); //Isso não irá acontecer, pois o campo de escrever so fica ativo quando seleciona um profissional
        }

        if (!text.isEmpty()) {//assim, quando há algo na String
            this.protocol.setNome(name); //seta o nome no protocol
            this.protocol.setTexto(text); //e seta o texto figitado na string

            //this.txtAreaReceive.append(" " + text + "\n"); //escreve na area de texto recebida com um append(append permute escrever de forma acumulada)

            this.conectaCliente.enviar(this.protocol); //chama o método enviar
        }

        this.txtAreaSend.setText(""); //limpa automaticamente a area apos enviar a msg
    }//GEN-LAST:event_btnEnviarActionPerformed

    private void jButtonAluAndChatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAluAndChatActionPerformed
        Color cor = Color.RED;
        String name = this.txtName.getText(); //pega o nome que esta na caixa de diálogo
        String profi = this.jTextSuaPro.getText();

        if (!name.isEmpty() && !profi.isEmpty()) { //Teste para não conectar sem nome, se tem nome digitado...
            this.protocol = new Protocol(); //INICIA(instancia) o objeto
            this.protocol.setStatus(Status.CONECTADO); //Seta o Status DO CLIENTE em Protocol
            this.protocol.setNome("[" + profi + "] " + name);// seta o nome do cliente

            this.conectaCliente = new ConectaCliente(); //inicia uma nova Thread socket que está enste objeto
            this.s = this.conectaCliente.connect(); //inicia o método conecta que retorna o socket

            new Thread(new ListenerSocket(this.s)).start(); //com os dados preenchidos, inicia a Thread

            this.conectaCliente.enviar(protocol); //chama o método enviar no conecta Cliente
            //this.jPanelInicial.setVisible(false);
            this.jPanelAlugarLocal.setVisible(false);
            this.jPanelChat.setVisible(true);            
            this.jLabelSeuNome.setText(profi + ": " + name);
        }
    }//GEN-LAST:event_jButtonAluAndChatActionPerformed

    private void btnCadastrarEspacoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCadastrarEspacoActionPerformed
        String tag = "***";
        String name = this.txtName.getText(); //pega o nome que esta na caixa de diálogo
        String local = this.txtLocal.getText();
        String h1, h2, h3, h4;
        if(bt1.isSelected()){
            h1 = "08 - 10, ";
            
        }else{
            h1 = "";
        }
         if(bt2.isSelected()){
            h2 = "10 - 12, ";
        }else{
            h2 = "";
        } if(bt3.isSelected()){
            h3 = "14 - 16, ";
        }else{
            h3 = "";
        } if(bt4.isSelected()){
            h4 = "16 - 18.";
        }else{
            h4 = "";
        }
        

        if (!name.isEmpty() && !local.isEmpty()) { //Teste para não conectar sem nome, se tem nome digitado...
            this.protocol = new Protocol(); //INICIA(instancia) o objeto
            this.protocol.setStatus(Status.CONECTADO); //Seta o Status DO CLIENTE em Protocol
            this.protocol.setNome("[" + name + "] " + local + h1+h2+h3+h4+"***");// seta o nome do cliente

            this.conectaCliente = new ConectaCliente(); //inicia uma nova Thread socket que está enste objeto
            this.s = this.conectaCliente.connect(); //inicia o método conecta que retorna o socket

            new Thread(new ListenerSocket(this.s)).start(); //com os dados preenchidos, inicia a Thread

            this.conectaCliente.enviar(protocol); //chama o método enviar no conecta Cliente
            //this.jPanelInicial.setVisible(false);
            this.jPanelLocatario.setVisible(false);
            this.jPanelAlugarLocal.setVisible(false);
            this.jPanelChat.setVisible(true);            
            this.jLabelSeuNome.setText(local + ": " + name);
        }

    }//GEN-LAST:event_btnCadastrarEspacoActionPerformed

    private void btnConnectarLocActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConnectarLocActionPerformed
        this.jPanelInicial.setVisible(false);
        this.jPanelLocatario.setVisible(true);
        this.jLabelNomeLocatario.setText(txtName.getText());
    }//GEN-LAST:event_btnConnectarLocActionPerformed

    private void btnConnectarProfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConnectarProfActionPerformed
        // TODO add your handling code here:
        this.jPanelInicial.setVisible(false);
        this.jPanelAlugarLocal.setVisible(true);
    }//GEN-LAST:event_btnConnectarProfActionPerformed

    private void btnConnectarClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConnectarClienteActionPerformed
        String name = this.txtName.getText(); //pega o nome que esta na caixa de diálogo

        if (!name.isEmpty()) { //Teste para não conectar sem nome, se tem nome digitado...
            this.protocol = new Protocol(); //INICIA(instancia) o objeto
            this.protocol.setStatus(Status.CONECTADO); //Seta o Status DO CLIENTE em Protocol
            this.protocol.setNome(name);// seta o nome do cliente

            this.conectaCliente = new ConectaCliente(); //inicia uma nova Thread socket que está enste objeto
            this.s = this.conectaCliente.connect(); //inicia o método conecta que retorna o socket

            new Thread(new ListenerSocket(this.s)).start(); //com os dados preenchidos, inicia a Thread

            this.conectaCliente.enviar(protocol); //chama o método enviar no conecta Cliente
            this.jLabelSeuNome.setText(name);
            this.listOnlines1.setVisible(false);
            this.jPanelInicial.setVisible(false);
            this.jPanelChat.setVisible(true);

        }
    }//GEN-LAST:event_btnConnectarClienteActionPerformed

    private void txtNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNameActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JRadioButton bt1;
    private javax.swing.JRadioButton bt2;
    private javax.swing.JRadioButton bt3;
    private javax.swing.JRadioButton bt4;
    private javax.swing.JButton btnCadastrarEspaco;
    private javax.swing.JButton btnConnectarCliente;
    private javax.swing.JButton btnConnectarLoc;
    private javax.swing.JButton btnConnectarProf;
    private javax.swing.JButton btnEnviar;
    private javax.swing.JButton btnLimpar;
    private javax.swing.JButton btnSair;
    private javax.swing.JButton jButtonAluAndChat;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabelNomeLocatario;
    private javax.swing.JLabel jLabelNomeProfissional;
    private javax.swing.JLabel jLabelSeuNome;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanelAlugarLocal;
    private javax.swing.JPanel jPanelChat;
    private javax.swing.JPanel jPanelInicial;
    private javax.swing.JPanel jPanelLocatario;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTextField jTextSuaPro;
    private javax.swing.JList listOnlines;
    private javax.swing.JList listOnlines1;
    private javax.swing.JTextArea txtAreaReceive;
    private javax.swing.JTextArea txtAreaSend;
    private javax.swing.JTextField txtLocal;
    private javax.swing.JTextField txtName;
    // End of variables declaration//GEN-END:variables
}
