(ns taggy.states.events
  (:require
   [re-frame.core :as rf]
   [taggy.states.db :as db]))

(defn find-label-by-type-id
  [id types]
  (if (= id :0)
    :+
    (-> #(= id (:id %1))
        (filter types)
        first
        :label)))

(rf/reg-event-db
 ::initialize-db
 (fn [_ _]
   db/default-db))

(rf/reg-event-db
 ::update-field
 (fn [db [_ id value]]
   (assoc-in db [:field-values id] (into #{} value))))

(rf/reg-event-db
 ::add-field
 (fn [db _]
   (update-in db [:field-ids]
              #(vec (conj %1 (-> %1 last inc))))))

(rf/reg-event-db
 ::select-tab
 (fn [db [_ new-type-id]]
   (let [types           (:all-types db)
         new-type-label  (find-label-by-type-id new-type-id types)]
     (if (= new-type-label :+)
       (update-in db [:all-types] #(conj %1 {:id 10 :label :new})) ;; Todo
       (assoc-in db [:current-type] {:id new-type-id
                                     :label new-type-label})))))
