(ns koans.14-recursion
  (:require [koan-engine.core :refer :all]))

(defn is-even? [n]
  (if (= n 0)
    true
    (not (is-even? (dec n)))))

(defn is-even-bigint? [n]
  (loop [n   n
         acc true]
    (if (= n 0)
      acc
      (recur (dec n) (not acc)))))

(defn recursive-reverse [coll]
  (let [len (count coll)]
    (if (= len 0)
      '()
      (cons (last coll) (recursive-reverse (take (- len 1) coll))))))

;; This blows up the stack
;;
;; (defn factorial [n]
;;   (cond (= n 0) 0
;;         (= n 1) 1
;;         :else (* n (factorial (- n 1)))))

;; These both fail. I think it's because the return variable gets initialized as a regular int
;; instead of a bigint, while in the version that works, the return var starts out as whatever you
;; pass in and the function is called below with a bigint
;;
;; (defn factorial [n]
;;   (loop [i 1
;;          acc 1]
;;     (if (> i n)
;;       acc
;;       (recur (inc n) (* acc i)))))
;;
;; (defn factorial [n]
;;   (reduce * (range 1 (inc n))))

(defn factorial [n]
  (loop [n n
         acc 1]
    (if (= n 1)
      acc
      (recur (dec n) (* acc n)))))

(meditations
 "Recursion ends with a base case"
 (= true (is-even? 0))

 "And starts by moving toward that base case"
 (= false (is-even? 1))

 "Having too many stack frames requires explicit tail calls with recur"
 (= false (is-even-bigint? 100003N))

 "Reversing directions is easy when you have not gone far"
 (= '(1) (recursive-reverse [1]))

 "Yet it becomes more difficult the more steps you take"
 (= '(6 5 4 3 2) (recursive-reverse [2 3 4 5 6]))

 "Simple things may appear simple."
 (= 1 (factorial 1))

 "They may require other simple steps."
 (= 2 (factorial 2))

 "Sometimes a slightly bigger step is necessary"
 (= 6 (factorial 3))

 "And eventually you must think harder"
 (= 24 (factorial 4))

 "You can even deal with very large numbers"
 (< 1000000000000000000000000N (factorial 1000N))

 "But what happens when the machine limits you?"
 (< 1000000000000000000000000N (factorial 100003N)))
