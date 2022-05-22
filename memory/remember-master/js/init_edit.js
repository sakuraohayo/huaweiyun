!function($) {
    'use strict';

    var AdvanceFormApp = function() {
        this.$body = $('body'),
        this.$window = $(window)
    };


    /** 
     * Initlizes the select2
    */
    AdvanceFormApp.prototype.initSelect2 = function() {
        // Select2
        $(".select2").select2({
            language: {
                noResults: function(params) {
                    return "暂无此标签，请选择已有标签";
                }
           }      
        });

        // Select2 with limiting
        $(".select2-limiting").select2({
            maximumSelectionLength: 2
        });
    },

    /** 
     * Initilize
    */
   AdvanceFormApp.prototype.init = function() {
        var $this = this;
        this.initSelect2();
    },

    $.AdvanceFormApp = new AdvanceFormApp, $.AdvanceFormApp.Constructor = AdvanceFormApp


}(window.jQuery),
    //initializing main application module
function($) {
    "use strict";
    $.AdvanceFormApp.init();
}(window.jQuery);
