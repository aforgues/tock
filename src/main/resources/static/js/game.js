function onTogglePawn(element, pawnNumber, isPlayable, targetPawnPosition) {
    if (isPlayable == 'true') {
        _togglePawn(element, pawnNumber, isPlayable, targetPawnPosition, 'active');
    }
    else {
        // Check that a pawn has already been selected to play
        let activePawns = document.getElementsByClassName("active");
        if (!activePawns || activePawns.length === 0)
            return false;

        // Select targeted pawn in case of JACK card
        let cardSelected = document.getElementById("card").value;
        if (cardSelected && cardSelected === "JACK" && targetPawnPosition) {
            _togglePawn(element, pawnNumber, isPlayable, targetPawnPosition, 'targeted');
        }
        else
            return false;
    }
}

function _togglePawn(element, pawnNumber, isPlayable, targetPawnPosition, classValue) {
    let formInputId = 'pawnNumber';
    let formInputValue = pawnNumber;
    if (classValue === 'targeted') {
        formInputId = 'targetPosition';
        formInputValue = targetPawnPosition;
    }

    if (element.className.endsWith(classValue)) {
        element.className = element.className.substring(0, element.className.indexOf(classValue)-1);
        document.getElementById(formInputId).value = '';
    }
    else {

        // first unselect other pawn with same classValue
        let pawns = document.getElementsByClassName(classValue);
        if (pawns && pawns.length > 0) {
            for (let i = 0; i < pawns.length; i++) {
                onTogglePawn(pawns[i], pawnNumber, isPlayable, targetPawnPosition);
            }
        }

        element.className = element.className + " " + classValue;
        document.getElementById(formInputId).value = formInputValue;
    }
}

function resetFormFields() {
    // TODO : ne faire ce reinit que pour les vraies erreurs (IllegalPawnMoveException), pas les oublis (champ obligatoires)
    document.getElementById('card').value='';
    document.getElementById('pawnNumber').value='';
    document.getElementById('targetPosition').value='';
}

/*function onCardSelect() {
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
}*/

function onCardClick(element, cardValue) {
    const classValue = 'selectedCard';
    const formInputId =  'card';
    if (element.className.endsWith(classValue)) {
        element.className = element.className.substring(0, element.className.indexOf(classValue)-1);
        document.getElementById(formInputId).value = '';
    }
    else {

        // first unselect other pawn with same classValue
        let cards = document.getElementsByClassName(classValue);
        if (cards && cards.length > 0) {
            for (let i = 0; i < cards.length; i++) {
                onCardClick(cards[i]);
            }
        }


        element.className = element.className + " " + classValue;
        document.getElementById(formInputId).value = cardValue;
    }
}