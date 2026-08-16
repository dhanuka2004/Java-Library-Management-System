public class Member {
    private String name;
    private String memberId;

    public Member(String name, String memberId) {
        this.name = name;
        this.memberId = memberId;
    }

    public String getMemberId() { 
        return memberId; 
    }
    
    public String getName() { 
        return name; 
    }
}