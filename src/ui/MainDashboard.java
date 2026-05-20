import dao.*;
import javax.swing.*;
import java.awt.*;

public class MainDashboard extends JFrame {

    private int tc,pl,al,oc,ds;

    public MainDashboard() {
        setTitle("LoanPro - Loan Management System");
        setSize(1000, 680);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        tc=new CustomerDAO().getAllCustomers().size();
        pl=new LoanDAO().getPendingLoans().size();
        al=new LoanDAO().getApprovedLoans().size();
        oc=new RepaymentDAO().getOverdueCount();
        ds=new RepaymentDAO().getDueSoonCount();
        initComponents();
    }

    private void initComponents() {
        setLayout(null);
        getContentPane().setBackground(new Color(31,56,100));

        JLabel t=new JLabel("LoanPro - Loan Management System");
        t.setFont(new Font("Arial",Font.BOLD,20));
        t.setForeground(Color.WHITE);
        t.setBounds(30,10,600,30);
        add(t);

        int ah=0;
        if(oc>0){ addAlert("OVERDUE: "+oc+" payment(s) overdue!",new Color(192,0,0),45); ah+=32; }
        if(ds>0){ addAlert("REMINDER: "+ds+" payment(s) due in 7 days!",new Color(255,140,0),45+ah); ah+=32; }

        int cy=80+ah;
        addCard("Customers",tc,new Color(46,117,182),30,cy);
        addCard("Pending",pl,new Color(192,80,77),200,cy);
        addCard("Approved",al,new Color(55,86,35),370,cy);
        addCard("Overdue",oc,oc>0?new Color(192,0,0):new Color(119,36,50),540,cy);

        int chy=cy+105;
        add(makePie(chy));
        add(makeBar(chy));

        JLabel ml=new JLabel("MAIN MENU");
        ml.setFont(new Font("Arial",Font.BOLD,14));
        ml.setForeground(Color.WHITE);
        ml.setBounds(670,chy,200,25);
        add(ml);

        addBtn("Customer Management",670,chy+30,new Color(46,117,182),e->new CustomerForm().setVisible(true));
        addBtn("Loan Application",670,chy+110,new Color(46,117,182),e->new LoanApplicationForm().setVisible(true));
        addBtn("Loan Approval",670,chy+190,new Color(192,80,77),e->new LoanApprovalForm().setVisible(true));
        addBtn("Repayment",860,chy+30,new Color(55,86,35),e->new RepaymentForm().setVisible(true));
        addBtn("Reports",860,chy+110,new Color(119,36,50),e->new ReportForm().setVisible(true));
        addBtn("Exit",860,chy+190,new Color(80,80,80),e->System.exit(0));

        JLabel f=new JLabel("LoanPro v1.0 | NIBM 2026 | EAD Coursework");
        f.setFont(new Font("Arial",Font.PLAIN,11));
        f.setForeground(new Color(173,216,230));
        f.setBounds(30,645,500,20);
        add(f);
    }

    private void addAlert(String msg,Color col,int y){
        JPanel p=new JPanel(null);
        p.setBounds(30,y,940,28);
        p.setBackground(col);
        JLabel l=new JLabel(msg);
        l.setFont(new Font("Arial",Font.BOLD,12));
        l.setForeground(Color.WHITE);
        l.setBounds(10,5,900,18);
        p.add(l);
        add(p);
    }

    private void addCard(String title,int val,Color col,int x,int y){
        JPanel p=new JPanel(null);
        p.setBounds(x,y,155,95);
        p.setBackground(col);
        JLabel v=new JLabel(String.valueOf(val));
        v.setFont(new Font("Arial",Font.BOLD,34));
        v.setForeground(Color.WHITE);
        v.setBounds(15,8,130,45);
        p.add(v);
        JLabel tl=new JLabel(title);
        tl.setFont(new Font("Arial",Font.PLAIN,12));
        tl.setForeground(new Color(220,220,220));
        tl.setBounds(15,55,130,25);
        p.add(tl);
        add(p);
    }

    private JPanel makePie(int chy){
        JPanel p=new JPanel(){
            protected void paintComponent(Graphics g){
                super.paintComponent(g);
                Graphics2D g2=(Graphics2D)g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(31,56,100));
                g2.setFont(new Font("Arial",Font.BOLD,12));
                g2.drawString("Loan Status - Pie Chart",50,20);
                int[]vals={al,pl,oc,1};
                Color[]cols={new Color(55,86,35),new Color(192,80,77),new Color(192,0,0),new Color(119,36,50)};
                String[]lbls={"Approved","Pending","Overdue","Rejected"};
                int total=0; for(int v:vals)total+=v; if(total==0)total=1;
                int start=0;
                for(int i=0;i<vals.length;i++){
                    int angle=i==vals.length-1?360-start:(int)(360.0*vals[i]/total);
                    g2.setColor(cols[i]); g2.fillArc(50,30,160,160,start,angle);
                    g2.setColor(Color.WHITE); g2.drawArc(50,30,160,160,start,angle);
                    start+=angle;
                    g2.setColor(cols[i]); g2.fillRect(20,205+(i*18),12,12);
                    g2.setColor(Color.BLACK); g2.setFont(new Font("Arial",Font.PLAIN,10));
                    g2.drawString(lbls[i]+"("+vals[i]+")",36,216+(i*18));
                }
            }
        };
        p.setBounds(30,chy,280,270);
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        return p;
    }

    private JPanel makeBar(int chy){
        JPanel p=new JPanel(){
            protected void paintComponent(Graphics g){
                super.paintComponent(g);
                Graphics2D g2=(Graphics2D)g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(31,56,100));
                g2.setFont(new Font("Arial",Font.BOLD,12));
                g2.drawString("Loan Overview - Bar Chart",50,20);
                String[]cats={"Customers","Pending","Approved","Overdue"};
                int[]vals={tc,pl,al,oc};
                Color[]cols={new Color(46,117,182),new Color(192,80,77),new Color(55,86,35),new Color(192,0,0)};
                int max=1; for(int v:vals)if(v>max)max=v;
                for(int i=0;i<vals.length;i++){
                    int bh=Math.max(5,(int)(180.0*vals[i]/max));
                    int x=30+i*65,y=220-bh;
                    g2.setColor(cols[i]); g2.fillRoundRect(x,y,45,bh,8,8);
                    g2.setColor(new Color(31,56,100)); g2.setFont(new Font("Arial",Font.BOLD,11));
                    g2.drawString(String.valueOf(vals[i]),x+15,y-5);
                    g2.setColor(Color.DARK_GRAY); g2.setFont(new Font("Arial",Font.PLAIN,10));
                    g2.drawString(cats[i],x+2,240);
                }
                g2.setColor(Color.GRAY); g2.drawLine(20,220,290,220);
            }
        };
        p.setBounds(330,chy,310,270);
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        return p;
    }

    private void addBtn(String text,int x,int y,Color col,java.awt.event.ActionListener a){
        JButton b=new JButton(text);
        b.setBounds(x,y,175,65);
        b.setBackground(col);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Arial",Font.BOLD,11));
        b.setFocusPainted(false);
        b.addActionListener(a);
        add(b);
    }

    public static void main(String[]args){
        SwingUtilities.invokeLater(()->new MainDashboard().setVisible(true));
    }
}
