$(document).ready(function() {
    console.log("loading jQuery functions");

    // show edit form to edit
    $(".btn-edit").click(function (e) {
        const parentNode = $(e.target).parents(".user-info");

        parentNode.siblings(".hidden-form").show();
        parentNode.hide();
    });

    // hide edit form when cancel button clicked
    $(".btn-cancel").click(function (e) {
        e.preventDefault();
        const parentNode = $(e.target).parents(".hidden-form");

        parentNode.hide();
        parentNode.siblings(".user-info").show()
    });

    // validateUserFullName();
    // function validateUserFullName() {
    //     console.log(firstName);
    //     const firstName = $("input[name=firstName]").val();
    //     const lastName = $("input[name=lastName]").val();
    //
    // }
    const serializedForm = $("#renterName").serialize();
    ajaxRequest(serializedForm);

    function ajaxRequest(serializedForm) {
        $.ajax({
            url: `/${endpoint}`,
            type: "POST",
            data: serializedForm,
            dataType: "json",
            success: function () {
                console.log("success");
            }
        });
    }
});
