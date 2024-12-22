function uploadFile(file, path) {

    const formData = new FormData();
    formData.append('file', file);
    formData.append('path', path);

    return new Promise((resolve, reject) => {

        $.ajax({
            url: "/api-common/uploadFile",
            method: "POST",
            data: formData,
            processData: false,
            contentType: false,
            success: function (result) {
                resolve(result);
            },
            error: function (xhr, status, error) {
                reject(error);
            }
        });
    });
}


function updateFile(beforeFile, newFile, path) {

    const formData = new FormData();
    formData.append('beforeFile', beforeFile);
    formData.append('newFile', newFile);
    formData.append('path', path);

    return new Promise((resolve, reject) => {

        $.ajax({
            url: "/api-common/updateFile",
            method: "POST",
            data: formData,
            processData: false,
            contentType: false,
            success: function (result) {
                resolve(result);
            },
            error: function (xhr, status, error) {
                reject(error);
            }
        });
    });
}