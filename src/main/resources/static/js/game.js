function onTogglePawn(element, pawnNumber, isPlayable, targetPawnPosition) {
    if (isPlayable == 'true') {
        if (element.className.endsWith("active")) {
            element.className = element.className.substring(0, element.className.indexOf("active")-1);
            document.getElementById('pawnNumber').value = '';
        }
        else {
            // first unselect other active pawn
            let activePawns = document.getElementsByClassName("active");
            if (activePawns && activePawns.length > 0) {
                for (let i=0; i < activePawns.length; i++) {
                    onTogglePawn(activePawns[i], pawnNumber, isPlayable, targetPawnPosition);
                }
            }

            element.className = element.className + " active";
            document.getElementById('pawnNumber').value = pawnNumber;
        }
    }
    else {
        // Check that a pawn has already been selected to play
        let activePawns = document.getElementsByClassName("active");
        if (!activePawns || activePawns.length === 0)
            return false;

        // Select targeted pawn in case of JACK card
        let cardSelected = document.getElementById("card").value;
        if (cardSelected && cardSelected === "JACK" && targetPawnPosition) {
            if (element.className.endsWith("targeted")) {
                element.className = element.className.substring(0, element.className.indexOf("targeted")-1);
                document.getElementById('targetPosition').value = '';
            }
            else {

                // first unselect other targeted pawn
                let targetedPawns = document.getElementsByClassName("targeted");
                if (targetedPawns && targetedPawns.length > 0) {
                    for (let i = 0; i < targetedPawns.length; i++) {
                        onTogglePawn(targetedPawns[i], pawnNumber, isPlayable, targetPawnPosition);
                    }
                }

                element.className = element.className + " targeted";
                document.getElementById('targetPosition').value = targetPawnPosition;
            }
        }
        else
            return false;
    }
}

function resetFormFields() {
    // TODO : ne faire ce reinit que pour les vraies erreurs (IllegalPawnMoveException), pas les oublis (champ obligatoires)
    document.getElementById('card').value='';
    document.getElementById('pawnNumber').value='';
    document.getElementById('targetPosition').value='';
}

function onCardSelect() {
    let selectedCard = document.getElementById('card').value;
    if (selectedCard !== "JACK") {
        // first unselect targeted pawn
        let targetedPawns = document.getElementsByClassName("targeted");
        if (targetedPawns && targetedPawns.length > 0) {
            for (let i = 0; i < targetedPawns.length; i++) {
                let element = targetedPawns[i];
                element.className = element.className.substring(0, element.className.indexOf("targeted")-1);
            }
        }
        document.getElementById('targetPosition').value = '';
    }
}