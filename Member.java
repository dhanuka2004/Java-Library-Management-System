public class Member {
    private String name;
    private String memberId;

    public Member(String memberId, String name) {
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