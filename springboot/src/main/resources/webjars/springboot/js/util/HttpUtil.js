/**
 * Created by Administrator on 2019/11/25.
 */
post = function (url, data, callback) {
    ajax(url, JSON.stringify(data), callback, "post");
};
get = function (url, data, callback) {
    ajax(url, data, callback, "get");
};

ajax = function (url, data, callback, model) {
    $.ajax({
        type: model,
        contentType: "application/json;charset=UTF-8",
        url: url,
        dataType: "json",
        data: data,
        async: false,//这个是同步方法
        success: function (result) {
            if (callback) {
                callback(result);
            }

        }, error: function (result) {
            alert(JSON.stringify(result));
        }
    });
}
