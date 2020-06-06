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

function restorePlayerChoices(currentPlayerPawnColor) {
    let cardValue = document.getElementById('card').value;
    if (cardValue) {
        onCardClick(document.getElementById('card-hearts-' + cardValue.toLowerCase()), cardValue);
    }

    let pawnNumberValue = document.getElementById('pawnNumber').value;
    if (pawnNumberValue) {
        let sourcePawnElement = document.getElementById(currentPlayerPawnColor + '-' + pawnNumberValue);
        onTogglePawn(sourcePawnElement, pawnNumberValue, 'true', '');
    }

    let targetPositionValue = document.getElementById('targetPosition').value;
    if (targetPositionValue) {
        let targetPawnElement = document.getElementById(targetPositionValue).firstElementChild;
        if (targetPawnElement) {
            onTogglePawn(targetPawnElement, '', 'false', targetPositionValue);
        }
    }
}

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