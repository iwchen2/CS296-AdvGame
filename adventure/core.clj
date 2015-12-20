(ns adventure.core
  (:require [clojure.core.match :refer [match]]
              [clojure.string :as str])
  (:gen-class))

(def the-map
  {:grand-foyer {:desc "The walls are freshly painted but do not have
                 any pictures.  You get the feeling it was just
                 created for some game or something."
           :title "in the foyer"
           :dir {:north :greathall
                 :south :grue-pen
                 :west :library
                 :east :living-room}
           :content [:raw-egg]} 
   :grue-pen {:desc "There is a grue here.  You are about to get eaten."
              :title "the grue pen"
              :dir {}
              :content [:grue]}
   :greathall {:desc "It is very short, but not too short."
              :title "the hallway"
              :dir {:south :grand-foyer
                    :north :kitchen 
                    :west :gallery
                    :east :ballroom}
              :content []}
   :kitchen {:desc "It looks like you could cook food here."
              :title "the kitchen"
              :dir {:south :greathall 
                    :east :dining-hall
                    :west :pantry
                    :down :wine-cellar}
              :content []}
   :wine-cellar {:desc "Lots of wine here."
                 :title "the wine cellar"
                 :dir{:up :kitchen}
                 :content []}
   :dining-hall {:desc "A place to eat."
                  :title "the dining hall"
                  :dir {:west :kitchen
                       :south :ballroom}
                  :content []}
   :gallery {:desc "Mona Lisa, The Starry Night, along with other paintings seem to located here"
              :title "The gallery"
              :dir {:east :greathall
                   :south :library}
              :content []}
   :ballroom {:desc "Illuminated chandliers, stained glass windows"
                :title "the ballroom"
                :dir {:west :greathall
                      :north :dining-hall
                      :south :living-room}
                :content []}
   :library {:desc "A quiet place with shelves of books."
              :title "the grand library"
              :dir {:north :gallery
                   :east :grand-foyer}
              :content []}
   :pantry {:desc "Lots and lots and lots of food. It's quite intimidating actually"
            :title "the pantry"
            :dir {:east :kitchen}
            :content []}
    :living-room {:desc "A good place to chill and relax"
                  :title "the living room"
                  :dir {:west :grand-foyer
                        :north :ballroom}
                  :content []}
    :gameroom {:desc "A place to play table games and arcade games"
               :title "the game room"
               :dir{:south :kitchen
                    :north :conservatory
                    :east :playroom}
               :content []}
    :conservatory {:desc "An abundance of plants and sunlight"
                   :title "the conservatory"
                   :dir {:south :gameroom}
                   :content []}
    :playroom {:desc "A place for children to play. Filled with toys and even a mini slide"
               :title "the playroom"
               :dir {:west :gameroom
                     :east :master-bedroom
                     :north :private-chamber}
               :content[]}
    :master-bedroom {:desc "where the smith's used to sleep, best I stay out of this room"
                     :title "the master bedroom"
                     :dir {:west :playroom
                           :east :master-bathroom}
                     :content []}
    :master-bathroom{:desc ""
                     :title "the master bathroom"
                     :dir {:west :master-bedroom}
                     :content []}
    :closet {:desc "lots of clothes"
             :title "a walk-in closet"
             :dir{:south :master-bedroom}
             :content []}
    :private-chamber {:desc "Just a boring room"
                      :title "the private chamber"
                      :dir {:south :playroom}
                      :content []}
})

(def adventurer
  {:location :grand-foyer
   :inventory #{}
   :before #{}})

(defn status [adv]
  (let [location (adv :location)]
    (print (str "You are " (-> the-map location :title) ". " ))
    (when-not ((adv :before) location)
      (print (-> the-map location :desc) ) )
    (update-in adv [:before] #(conj % location))))

(defn go [dir adv]
  (let [curr-room (get-in adv [:location])]
   (if-let [dest (get-in the-map [curr-room :dir dir])]
     (assoc-in adv [:location] dest)
     (println "You cannot go that direction."))))

(defn respond [inst adv]
  (match inst
         [:north] (go :north adv)
         [:east] (go :east adv)
         [:south] (go :south adv)
         [:west] (go :west adv)
         [:up] (go :up adv)
         [:down] (go :down adv)
         _ (do 
             (println "I'm sorry Dave.  I cannot allow you to do that.")
             adv) ) )

(defn to-keywords [st]
   (mapv keyword (str/split st #" +")))

(defn -main
  [& args]
  (println "Welcome to the Uncooked Egg Adventure!")
  (loop [the-m the-map
         the-a adventurer]
    (let [the-a' (status the-a)
          _      (println "What do you want to do?")
          inst   (read-line) ]
      (recur the-m (respond (to-keywords inst) the-a'))
      ) ) )
