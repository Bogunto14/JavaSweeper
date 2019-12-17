package solver;

public enum MoveType {
    First("Первый ход"),
    Logical("Логический ход"),
    Probability("Вероятностный ход"),
    Random("Случайный ход"),
    NoStep("Игра закончена");

    MoveType(String _message){
        message = _message;
    }
    private String message;
    public String getMessage(){
        return message;
    }
}
